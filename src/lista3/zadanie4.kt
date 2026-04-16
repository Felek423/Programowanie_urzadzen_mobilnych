package lista3

fun countElements(list: List<List<String>>): Map<String, Int> =
    list.flatten()
        .groupBy { it }
        .mapValues { it.value.size }

fun main() {
    val input = listOf(listOf("a", "b", "c"), listOf("c", "d", "f"), listOf("d", "f", "g"))
    println(countElements(input))
}