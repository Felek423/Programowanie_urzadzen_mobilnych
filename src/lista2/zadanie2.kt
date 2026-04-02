package lista2

// Zwraca listę wszystkich elementów oprócz pierwszego
val <T> List<T>.tail: List<T>
    get() = this.drop(1)

// Zwraca pierwszy element listy
val <T> List<T>.head: T
    get() = this.first()

// Funkcja main do przetestowania działania
fun main() {
    val liczby = listOf(1, 2, 3, 4, 5)

    println("Oryginalna lista: $liczby")
    println("Head (pierwszy element): ${liczby.head}")
    println("Tail (reszta listy): ${liczby.tail}")

    val slowa = listOf("Jabłko", "Banan", "Wiśnia")
    println("\nHead dla słów: ${slowa.head}")
    println("Tail dla słów: ${slowa.tail}")
}