package lista3

fun findDuplicates(list: List<Int>): List<Int> {
    val seen = mutableSetOf<Int>()
    return list.filter { !seen.add(it) }.toSet().sorted()
}

fun main() {
    val lst = listOf(0, 1, 1, 1, 4, 4, 4, 9, 3, 3, 3, 3, 3, 3)
    println(findDuplicates(lst))
}