package com.example.lista1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@Serializable
object HomeRoute

@Serializable
object CategoriesRoute

@Serializable
object CartRoute

@Serializable
object FavoritesRoute

// Trasa dla szczegółów przyjmuje id produktu jako argument
@Serializable
data class DetailsRoute(val id: Int)

// Graf zagnieżdżony Checkout
@Serializable
object CheckoutGraph

@Serializable
object AddressRoute

@Serializable
object PaymentRoute

@Serializable
object SummaryRoute


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeShopApp()
        }
    }
}

@Composable
fun ComposeShopApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBarPlaceholder()
        }
    ) { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable<HomeRoute> {
                HomeScreen(
                    onNavigateToDetails = { productId ->
                        navController.navigate(DetailsRoute(id = productId))
                    }
                )
            }

            // Ekran Kategorii
            composable<CategoriesRoute> {
                CategoriesScreen()
            }

            // Ekran Koszyka
            composable<CartRoute> {
                CartScreen()
            }

            // Ekran Ulubionych
            composable<FavoritesRoute> {
                FavoritesScreen()
            }


            composable<DetailsRoute> { backStackEntry ->

                Text("Ekran szczegółów (placeholder)")
            }


             composable<AddressRoute> { Text("Ekran adresu (placeholder)") }
        }
    }
}


@Composable
fun HomeScreen(onNavigateToDetails: (Int) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ekran Start (Lista produktów)")
    }
}

@Composable
fun CategoriesScreen() {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
         Text("Ekran Kategorii")
    }
}

@Composable
fun CartScreen() {
     Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
         Text("Ekran Koszyka")
    }
}

@Composable
fun FavoritesScreen() {
     Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
         Text("ulubione")
    }
}

@Composable
fun BottomBarPlaceholder() {
     Text("dolny pasek", modifier = Modifier.padding(16.dp))
}