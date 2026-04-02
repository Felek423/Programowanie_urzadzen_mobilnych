package lista2

fun safeParseAndClassify(input: String?): String {
    // Sprawdzamy czy string jest nullem lub jest pusty
    if (input.isNullOrEmpty()) {
        return "BRAK_DANYCH"
    }

    // Bezpieczne parsowanie do liczby bez użycia '!!'
    // Jeśli wpisano np. "abc", toIntOrNull() zwróci null, co obsłużymy operatorem Elvisa ?:
    val number = input.toIntOrNull() ?: return "BRAK_DANYCH"

    // Sprawdzanie parzystości (reszta z dzielenia przez 2)
    return if (number % 2 == 0) {
        "PARZYSTA"
    } else {
        "NIEPARZYSTA"
    }
}

fun main() {

    // Test: poprawna liczba parzysta
    println("Test '4': ${safeParseAndClassify("4")}") // Oczekiwane: PARZYSTA

    // Test: poprawna liczba nieparzysta
    println("Test '7': ${safeParseAndClassify("7")}") // Oczekiwane: NIEPARZYSTA

    // Test (dodatkowy): błędny ciąg znaków (nie liczba)
    println("Test 'abc': ${safeParseAndClassify("abc")}") // Oczekiwane: BRAK_DANYCH
}