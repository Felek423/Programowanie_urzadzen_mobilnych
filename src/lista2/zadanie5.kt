package lista2

fun check(N: Int, list: List<Int>): Int {
    // Zaczynamy sprawdzanie od elementu na indeksie N
    for (i in N until list.size) {
        val current = list[i]
        val preamble = list.subList(i - N, i) // Pobieramy N poprzednich elementów

        // Sprawdzamy, czy w preambule istnieją dwie RÓŻNE liczby dające sumę 'current'
        val isValid = preamble.indices.any { j ->
            (j + 1 until preamble.size).any { k ->
                preamble[j] + preamble[k] == current
            }
        }

        // Jeśli nie znaleźliśmy takiej pary, zwracamy ten element
        if (!isValid) {
            return current
        }
    }

    // Jeśli wszystkie elementy spełniają warunek
    return -1
}

fun main() {
    // Przykład 1 (z obrazka wprowadzającego)
    val test1 = check(3, listOf(1, 2, 3, 5, 7, 12, 30))
    println("Test 1: $test1") // Oczekiwane: 30

    // Przykład 2 (verbatim)
    val test2 = check(2, listOf(1, 2, 3, 4, 5, 6))
    println("Test 2: $test2") // Oczekiwane: 4

    // Przykład 3 (dłuższa lista)
    val test3 = check(5, listOf(35, 25, 15, 25, 47, 40, 62, 55, 65, 95, 102, 117, 150, 182, 127, 219, 299, 277, 309, 576))
    println("Test 3: $test3") // Oczekiwane: 127
}