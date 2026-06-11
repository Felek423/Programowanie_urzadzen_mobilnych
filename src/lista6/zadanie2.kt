package com.example.lista6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

class Zadanie2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Uruchamiamy główny kontener z nawigacją
                    MainNavigationContainer()
                }
            }
        }
    }
}

// struktury danych
data class Task(val id: Int, val description: String, val maxPoints: Int)
data class AssignmentList(val id: String, val subject: String, val listNumber: Int, val grade: Double, val tasks: List<Task>)

val sampleAssignmentLists = listOf(
    AssignmentList("PUM1_L1", "Programowanie Urządzeń Mobilnych 1", 1, 4.5, listOf(
        Task(1, "Implementacja FizzBuzz", 3), Task(2, "Sprawdzenie palindromu", 3), Task(3, "Trójkąt Pascala", 4)
    )),
    AssignmentList("PUM1_L2", "Programowanie Urządzeń Mobilnych 1", 2, 5.0, listOf(
        Task(1, "Funkcje rozszerzające", 4), Task(2, "Funkcje wyższego rzędu", 6)
    )),
    AssignmentList("SO_L1", "Systemy Operacyjne", 1, 3.5, listOf(
        Task(1, "Implementacja semafora", 5), Task(2, "Problem producenta-konsumenta", 5)
    ))
)

// trasy nawigacji
sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object AssignmentLists : Screen("assignment_lists", "Listy zadań", Icons.Default.List)
    object GradesSummary : Screen("grades_summary", "Oceny", Icons.Default.Star)
    object ListDetail : Screen("list_detail/{listId}", "Szczegóły") {
        fun createRoute(listId: String) = "list_detail/$listId"
    }
}

// glowny kontener nawigacji
@Composable
fun MainNavigationContainer() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val bottomScreens = listOf(Screen.AssignmentLists, Screen.GradesSummary)
            // Wyświetlamy dolny pasek tylko na głównych ekranach E1 i E2
            if (bottomScreens.any { it.route == currentRoute }) {
                NavigationBar {
                    bottomScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.AssignmentLists.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.AssignmentLists.route) {
                AssignmentListsScreen(onListClick = { listId ->
                    navController.navigate(Screen.ListDetail.createRoute(listId))
                })
            }
            composable(Screen.GradesSummary.route) {
                GradesSummaryScreen()
            }
            composable(
                route = Screen.ListDetail.route,
                arguments = listOf(navArgument("listId") { type = NavType.StringType })
            ) { backStackEntry ->
                val listId = backStackEntry.arguments?.getString("listId")
                ListDetailScreen(listId = listId, onBackClick = { navController.popBackStack() })
            }
        }
    }
}

// 1 ekran listy zadan
@Composable
fun AssignmentListsScreen(onListClick: (String) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Text("Wszystkie listy", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 8.dp))
        }
        items(sampleAssignmentLists) { list ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onListClick(list.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = list.subject, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Lista nr ${list.listNumber}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Zadań: ${list.tasks.size}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Ocena: ${list.grade}", style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
                    }
                }
            }
        }
    }
}

// ekran 2 podsumowanie ocen
@Composable
fun GradesSummaryScreen() {
    val averages = sampleAssignmentLists.groupBy { it.subject }.mapValues { entry ->
        entry.value.map { it.grade }.average()
    }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Text("Średnie ocen", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 8.dp))
        }
        items(averages.toList()) { (subject, avg) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = subject, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Text(text = String.format("%.2f", avg), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

// ekran 3 szczegoly listy
@Composable
fun ListDetailScreen(listId: String?, onBackClick: () -> Unit) {
    val list = sampleAssignmentLists.find { it.id == listId }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBackClick, modifier = Modifier.padding(bottom = 12.dp)) {
            Text("Powrót")
        }

        if (list != null) {
            Text(text = list.subject, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Szczegóły listy nr ${list.listNumber}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(list.tasks) { task ->
                    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Zadanie ${task.id}", style = MaterialTheme.typography.titleSmall)
                                Text(text = "Max pkt: ${task.maxPoints}", style = MaterialTheme.typography.labelLarge)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        } else {
            Text("Błąd: Nie znaleziono listy.", color = MaterialTheme.colorScheme.error)
        }
    }
}