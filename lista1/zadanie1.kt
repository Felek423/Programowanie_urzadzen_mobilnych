package com.example.composeshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.hasRoute
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable object Home
@Serializable object Categories
@Serializable object Cart
@Serializable object Favorites
@Serializable object Profile
@Serializable object Orders
@Serializable object Settings
@Serializable data class Details(val id: Int)
@Serializable object Checkout
@Serializable object Address
@Serializable object Payment
@Serializable object Summary

data class Product(val id: Int, val name: String, val price: Double, val category: String)
data class Order(val id: Int, val date: String, val status: String)
data class Tab(val label: String, val route: Any, val icon: ImageVector)

val products = listOf(
    Product(1, "Laptop", 3499.0, "Elektronika"),
    Product(2, "Słuchawki", 299.0, "Elektronika"),
    Product(3, "Mysz", 99.0, "Elektronika"),
    Product(4, "Koszulka", 59.0, "Odzież"),
    Product(5, "Bluza", 149.0, "Odzież"),
    Product(6, "Czapka", 39.0, "Odzież")
)
val categories = products.map { it.category }.distinct()
val cartItems = products.take(3)
val paymentMethods = listOf("Karta", "BLIK", "Przelew")

val tabs = listOf(
    Tab("Start", Home, Icons.Default.Home),
    Tab("Kategorie", Categories, Icons.AutoMirrored.Filled.List),
    Tab("Koszyk", Cart, Icons.Default.ShoppingCart),
    Tab("Ulubione", Favorites, Icons.Default.Favorite)
)
val drawerItems = listOf(
    Triple("Profil", Profile, Icons.Default.Person),
    Triple("Historia zamówień", Orders, Icons.Default.Info),
    Triple("Ustawienia", Settings, Icons.Default.Settings)
)

fun price(p: Double) = "%.2f zł".format(p)

class ShopViewModel : ViewModel() {
    val favorites = mutableStateListOf<Int>()
    val orders = mutableStateListOf(
        Order(1, "2026-10-01", "Dostarczono"),
        Order(2, "2026-11-10", "W drodze")
    )
    var recipient by mutableStateOf("")
    var street by mutableStateOf("")
    var number by mutableStateOf("")
    var zip by mutableStateOf("")
    var city by mutableStateOf("")
    var payment by mutableStateOf<String?>(null)
    var darkMode by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)

    fun toggleFavorite(id: Int) {
        if (id in favorites) favorites.remove(id) else favorites.add(id)
    }

    fun addressValid() = listOf(recipient, street, number, zip, city).all { it.trim().isNotEmpty() }

    fun confirm() {
        orders.add(0, Order(orders.size + 1, java.time.LocalDate.now().toString(), "Przyjęto"))
        recipient = ""
        street = ""
        number = ""
        zip = ""
        city = ""
        payment = null
        message = "Zamówienie zostało przyjęte"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: ShopViewModel = viewModel()
            MaterialTheme(colorScheme = if (vm.darkMode) darkColorScheme() else lightColorScheme()) {
                ShopApp(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopApp(vm: ShopViewModel) {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val dest = entry?.destination
    val inCheckout = dest?.hierarchy?.any { it.hasRoute<Checkout>() } == true
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(inCheckout) {
        if (inCheckout) drawerState.close()
    }
    LaunchedEffect(vm.message) {
        vm.message?.let {
            snack.showSnackbar(it)
            vm.message = null
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !inCheckout,
        drawerContent = {
            ModalDrawerSheet {
                drawerItems.forEach { (label, route, icon) ->
                    NavigationDrawerItem(
                        label = { Text(label) },
                        icon = { Icon(icon, null) },
                        selected = dest?.hierarchy?.any { it.hasRoute(route::class) } == true,
                        onClick = {
                            nav.navigate(route) { launchSingleTop = true }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (inCheckout) "Zamówienie" else "ComposeShop") },
                    navigationIcon = {
                        if (!inCheckout) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, "Menu")
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (!inCheckout) {
                    NavigationBar {
                        tabs.forEach { tab ->
                            NavigationBarItem(
                                selected = dest?.hierarchy?.any { it.hasRoute(tab.route::class) } == true,
                                onClick = {
                                    nav.navigate(tab.route) {
                                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icon, null) },
                                label = { Text(tab.label) }
                            )
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(snack) }
        ) { padding ->
            NavHost(nav, startDestination = Home, modifier = Modifier.padding(padding)) {
                composable<Home> {
                    ProductList(products, vm.favorites, { nav.navigate(Details(it)) }, vm::toggleFavorite)
                }
                composable<Categories> {
                    CategoriesScreen(vm.favorites, { nav.navigate(Details(it)) }, vm::toggleFavorite)
                }
                composable<Cart> {
                    CartScreen { nav.navigate(Checkout) }
                }
                composable<Favorites> {
                    ProductList(
                        products.filter { it.id in vm.favorites },
                        vm.favorites,
                        { nav.navigate(Details(it)) },
                        vm::toggleFavorite
                    )
                }
                composable<Details> { e ->
                    val id = e.toRoute<Details>().id
                    DetailsScreen(products.first { it.id == id }) { nav.popBackStack() }
                }
                composable<Profile> { ProfileScreen() }
                composable<Orders> { OrdersScreen(vm.orders) }
                composable<Settings> { SettingsScreen(vm.darkMode) { vm.darkMode = it } }
                navigation<Checkout>(startDestination = Address) {
                    composable<Address> {
                        AddressScreen(vm, { nav.popBackStack() }) { nav.navigate(Payment) }
                    }
                    composable<Payment> {
                        PaymentScreen(vm.payment, { vm.payment = it }, { nav.popBackStack() }) {
                            nav.navigate(Summary)
                        }
                    }
                    composable<Summary> {
                        SummaryScreen(vm, { nav.popBackStack() }) {
                            vm.confirm()
                            nav.navigate(Home) {
                                popUpTo<Home> { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductList(
    list: List<Product>,
    favs: List<Int>,
    onDetails: (Int) -> Unit,
    onFav: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (list.isEmpty()) {
        Text("Brak produktów", modifier = modifier.padding(16.dp))
        return
    }
    LazyColumn(modifier.fillMaxSize()) {
        items(list, key = { it.id }) { p ->
            ListItem(
                headlineContent = { Text(p.name) },
                supportingContent = { Text("${p.category} • ${price(p.price)}") },
                trailingContent = {
                    IconButton(onClick = { onFav(p.id) }) {
                        Icon(
                            if (p.id in favs) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            "Ulubione"
                        )
                    }
                },
                modifier = Modifier.clickable { onDetails(p.id) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun CategoriesScreen(favs: List<Int>, onDetails: (Int) -> Unit, onFav: (Int) -> Unit) {
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected == null, { selected = null }, { Text("Wszystkie") })
            categories.forEach {
                FilterChip(selected == it, { selected = it }, { Text(it) })
            }
        }
        ProductList(
            products.filter { selected == null || it.category == selected },
            favs, onDetails, onFav,
            Modifier.weight(1f)
        )
    }
}

@Composable
fun CartScreen(onCheckout: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        cartItems.forEach { Text("${it.name} – ${price(it.price)}") }
        Text("Razem: ${price(cartItems.sumOf { it.price })}", style = MaterialTheme.typography.titleMedium)
        Button(onClick = onCheckout) { Text("Przejdź do zamówienia") }
    }
}

@Composable
fun DetailsScreen(p: Product, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(p.name, style = MaterialTheme.typography.headlineMedium)
        Text("Cena: ${price(p.price)}")
        Text("Kategoria: ${p.category}")
        Button(onClick = onBack) { Text("Wstecz") }
    }
}

@Composable
fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Jan Kowalski", style = MaterialTheme.typography.headlineMedium)
        Text("jan.kowalski@example.com")
        Text("Wrocław")
    }
}

@Composable
fun OrdersScreen(orders: List<Order>) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(orders, key = { it.id }) {
            ListItem(
                headlineContent = { Text("Zamówienie #${it.id}") },
                supportingContent = { Text("${it.date} • ${it.status}") }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun SettingsScreen(dark: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Ciemny motyw")
        Switch(dark, onChange)
    }
}

@Composable
fun Field(label: String, value: String, onChange: (String) -> Unit, showErrors: Boolean) {
    val error = showErrors && value.trim().isEmpty()
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        isError = error,
        supportingText = { if (error) Text("Pole wymagane") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AddressScreen(vm: ShopViewModel, onBack: () -> Unit, onNext: () -> Unit) {
    var showErrors by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Field("Odbiorca", vm.recipient, { vm.recipient = it }, showErrors)
        Field("Ulica", vm.street, { vm.street = it }, showErrors)
        Field("Numer", vm.number, { vm.number = it }, showErrors)
        Field("Kod pocztowy", vm.zip, { vm.zip = it }, showErrors)
        Field("Miasto", vm.city, { vm.city = it }, showErrors)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onBack) { Text("Wstecz") }
            Button(onClick = { if (vm.addressValid()) onNext() else showErrors = true }) { Text("Dalej") }
        }
    }
}

@Composable
fun PaymentScreen(
    selected: String?,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var showError by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        paymentMethods.forEach {
            Row(Modifier.clickable { onSelect(it) }) {
                RadioButton(selected == it, { onSelect(it) })
                Text(it, Modifier.padding(top = 12.dp))
            }
        }
        if (showError && selected == null) {
            Text("Wybierz metodę płatności", color = MaterialTheme.colorScheme.error)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onBack) { Text("Wstecz") }
            Button(onClick = { if (selected != null) onNext() else showError = true }) { Text("Podsumowanie") }
        }
    }
}

@Composable
fun SummaryScreen(vm: ShopViewModel, onBack: () -> Unit, onConfirm: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        cartItems.forEach { Text("${it.name} – ${price(it.price)}") }
        Text("Razem: ${price(cartItems.sumOf { it.price })}", style = MaterialTheme.typography.titleMedium)
        Text("Adres: ${vm.recipient}, ${vm.street} ${vm.number}, ${vm.zip} ${vm.city}")
        Text("Płatność: ${vm.payment ?: "-"}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onBack) { Text("Wstecz") }
            Button(onClick = onConfirm) { Text("Potwierdź zamówienie") }
        }
    }
}