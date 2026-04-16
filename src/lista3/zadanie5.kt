package lista3

fun evenPositiveSquare(list: List<Int>): List<Int> =
    list.filterIndexed { index, _ -> index % 2 != 0 }
        .filter { it > 0 }
        .map { it * it }

fun main() {
    val input = listOf(1, 2, 3, 5, -6, -1, -1, 2, 3)
    println(evenPositiveSquare(input))
}