package lista2

// Funkcja sprawdzająca czy lista jest posortowana
fun <A> isSorted(lst: List<A>, order: (A, A) -> Boolean): Boolean {
    // Pusta lista lub z jednym elementem jest zawsze posortowana
    if (lst.size <= 1) return true

    // Sprawdzamy elementy parami (i oraz i+1)
    for (i in 0 until lst.size - 1) {
        if (!order(lst[i], lst[i + 1])) {
            return false // Jeśli warunek nie jest spełniony, przerywamy
        }
    }

    return true
}

fun main() {
    // Przykład 1: Rosnąco
    val wynik1 = isSorted(listOf(1, 2, 3, 4)) { i: Int, j: Int -> i < j }
    println("Test 1: $wynik1") // Oczekiwane: true

    // Przykład 2: Równe
    val wynik2 = isSorted(listOf(1, 1, 1, 1)) { i: Int, j: Int -> i == j }
    println("Test 2: $wynik2") // Oczekiwane: true

    // Przykład 3: Alfabetycznie po pierwszej literze
    val wynik3 = isSorted(listOf("ahyyhh", "bkjn", "cnn", "duu")) { i: String, j: String ->
        i.first() < j.first()
    }
    println("Test 3: $wynik3") // Oczekiwane: true

    // Przykład 4 (Dodatkowy): Nieposortowana lista (powinno dać false)
    val wynik4 = isSorted(listOf(1, 5, 2, 4)) { i: Int, j: Int -> i < j }
    println("Test 4: $wynik4") // Oczekiwane: false
}