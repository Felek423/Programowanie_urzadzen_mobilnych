package com.example.lista1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.time.measureTimedValue

data class Movie(val id: Int, val title: String, val director: String, val year: Int, val genre: String)
data class MovieDetails(val movie: Movie, val description: String)
data class Review(val author: String, val text: String)
data class FullData(val details: MovieDetails, val reviews: List<Review>, val similar: List<Movie>)

private val movies = listOf(
    Movie(1, "Matrix", "Wachowscy", 1999, "Sci-Fi"),
    Movie(2, "Incepcja", "Christopher Nolan", 2010, "Sci-Fi"),
    Movie(3, "Pulp Fiction", "Quentin Tarantino", 1994, "Kryminał"),
    Movie(4, "Gladiator", "Ridley Scott", 2000, "Dramat"),
    Movie(5, "Forrest Gump", "Robert Zemeckis", 1994, "Dramat"),
    Movie(6, "Shrek", "Andrew Adamson", 2001, "Animacja"),
    Movie(7, "Titanic", "James Cameron", 1997, "Romans"),
    Movie(8, "Szczęki", "Steven Spielberg", 1975, "Thriller"),
    Movie(9, "Skazani na Shawshank", "Frank Darabont", 1994, "Dramat"),
    Movie(10, "Ojciec chrzestny", "Francis Ford Coppola", 1972, "Kryminał")
)

//  cztery funkcje suspend
suspend fun fetchPopularMovies(): List<Movie> {
    delay(2000)
    return movies
}

suspend fun fetchMovieDetails(movieId: Int): MovieDetails {
    delay(1500)
    val m = movies.first { it.id == movieId }
    return MovieDetails(m, "„${m.title}” to film z gatunku ${m.genre}, w reżyserii ${m.director}.")
}

suspend fun fetchMovieReviews(movieId: Int): List<Review> {
    delay(3000)
    val m = movies.first { it.id == movieId }
    return listOf(Review("Anna", "„${m.title}” to klasyka, polecam!"), Review("Piotr", "Dobry, choć trochę długi."))
}

suspend fun fetchSimilarMovies(movieId: Int): List<Movie> {
    delay(2000)
    // trzy kolejne filmy z listy, więc nigdy nie wskaże samego siebie
    return (1..3).map { movies[(movieId - 1 + it) % movies.size] }
}

// trzy async w jednym coroutineScope: najpierw start wszystkich, potem await
suspend fun loadConcurrent(id: Int): FullData = coroutineScope {
    val details = async { fetchMovieDetails(id) }
    val reviews = async { fetchMovieReviews(id) }
    val similar = async { fetchSimilarMovies(id) }
    FullData(details.await(), reviews.await(), similar.await())
}

// to samo, ale po kolei, bez async
suspend fun loadSequential(id: Int) =
    FullData(fetchMovieDetails(id), fetchMovieReviews(id), fetchSimilarMovies(id))

//  nawigacja
@Serializable object ListRoute
@Serializable data class DetailsRoute(val movieId: Int)

private val Gold = Color(0xFFFFC107)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val colors = darkColorScheme(
            primary = Gold, background = Color(0xFF0E0E14), surface = Color(0xFF1C1C28), onPrimary = Color.Black
        )
        setContent { MaterialTheme(colorScheme = colors) { Surface(Modifier.fillMaxSize()) { App() } } }
    }
}

@Composable
fun App() {
    val nav = rememberNavController()
    var list by remember { mutableStateOf<List<Movie>?>(null) }

    NavHost(nav, startDestination = ListRoute) {
        composable<ListRoute> {
            ListScreen(list, { list = it }) { nav.navigate(DetailsRoute(it)) }
        }
        composable<DetailsRoute> { entry ->
            val id = entry.toRoute<DetailsRoute>().movieId
            DetailsScreen(id) { nav.popBackStack() }
        }
    }
}

// pomocnicze elementy wyglądu
@Composable
fun Chip(text: String) = Surface(shape = RoundedCornerShape(50), color = Gold.copy(alpha = 0.2f)) {
    Text(text, Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = Gold, style = MaterialTheme.typography.labelMedium)
}

@Composable
fun Section(text: String) =
    Text(text, Modifier.padding(top = 20.dp, bottom = 8.dp), color = Gold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

//  ekran listy
@Composable
fun ListScreen(list: List<Movie>?, onLoaded: (List<Movie>) -> Unit, onClick: (Int) -> Unit) {
    LaunchedEffect(Unit) { if (list == null) onLoaded(fetchPopularMovies()) }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Text("KINO GURU", Modifier.padding(20.dp), color = Gold, fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineMedium)
        if (list == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = Gold) }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(list) { m ->
                    Card(Modifier.fillMaxWidth().clickable { onClick(m.id) }, shape = RoundedCornerShape(20.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(52.dp).clip(CircleShape).background(Gold), Alignment.Center) {
                                Text(m.title.first().toString(), color = Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(m.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text("${m.director} • ${m.year}", style = MaterialTheme.typography.bodySmall)
                                Spacer(Modifier.height(6.dp))
                                Chip(m.genre)
                            }
                        }
                    }
                }
            }
        }
    }
}

//ekran szczegółów
@Composable
fun DetailsScreen(id: Int, onBack: () -> Unit) {
    var data by remember { mutableStateOf<FullData?>(null) }
    var loading by remember { mutableStateOf(true) }
    var concurrent by remember { mutableStateOf(true) }
    var info by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    suspend fun load() {
        loading = true
        data = null
        val usedConcurrent = concurrent
        val (result, time) = measureTimedValue {
            if (usedConcurrent) loadConcurrent(id) else loadSequential(id)
        }
        data = result
        info = "${if (usedConcurrent) "Współbieżnie" else "Sekwencyjnie"}: %.2f s".format(time.inWholeMilliseconds / 1000.0)
        loading = false
    }

    LaunchedEffect(id) { load() }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
        // czysty nagłówek bez gradientu
        item {
            Box(Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp)) {
                Column {
                    TextButton(onBack) { Text("← Wstecz", color = Gold) }
                    Text(data?.details?.movie?.title ?: "Ładowanie…", fontWeight = FontWeight.Black, style = MaterialTheme.typography.headlineLarge)
                    data?.details?.movie?.let {
                        Text("${it.director} • ${it.year}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(4.dp))
                        Chip(it.genre)
                    }
                }
            }
        }
        // wybór trybu i przycisk ponownego ładowania
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(concurrent, { concurrent = true }, { Text("Współbieżnie") })
                    FilterChip(!concurrent, { concurrent = false }, { Text("Sekwencyjnie") })
                }
                Button(onClick = {scope.launch { load() } }, enabled = !loading, modifier = Modifier.padding(top = 8.dp)) { Text("Wczytaj ponownie") }
                if (info.isNotEmpty()) Card(Modifier.fillMaxWidth().padding(top = 12.dp)) {
                    Text("Czas pobierania: $info", Modifier.padding(16.dp), color = Gold, fontWeight = FontWeight.Bold)
                }
            }
        }
        val d = data
        if (loading || d == null) {
            item { Box(Modifier.fillMaxWidth().padding(48.dp), Alignment.Center) { CircularProgressIndicator(color = Gold) } }
        } else {
            item { Column(Modifier.padding(horizontal = 16.dp)) { Section("Opis"); Text(d.details.description) } }
            item { Column(Modifier.padding(horizontal = 16.dp)) { Section("Recenzje") } }
            items(d.reviews) {
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Column(Modifier.padding(16.dp)) { Text(it.author, fontWeight = FontWeight.Bold); Text(it.text) }
                }
            }
            item {
                Column {
                    Box(Modifier.padding(horizontal = 16.dp)) { Section("Podobne filmy") }
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(d.similar) {
                            Card(Modifier.width(140.dp)) {
                                Column(Modifier.padding(12.dp)) { Text(it.title, fontWeight = FontWeight.Bold); Text("${it.year}", style = MaterialTheme.typography.bodySmall) }
                            }
                        }
                    }
                }
            }
        }
    }
}
