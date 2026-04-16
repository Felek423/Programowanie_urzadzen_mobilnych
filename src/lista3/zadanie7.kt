package lista3

fun srt(list: List<String>): List<Pair<String, List<String>>> =
    list.filter { it.length % 2 == 0 }
        .groupBy { it.first().toString() }
        .toList()
        .sortedBy { it.first }

fun main() {
    val input = listOf(
        "cherry",
        "blueberry",
        "citrus",
        "apple",
        "apricot",
        "banana",
        "coconut"
    )
    println(srt(input))
}