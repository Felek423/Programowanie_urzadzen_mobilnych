package lista3

fun perm(list: List<Int>): List<List<Int>> {
    if (list.isEmpty()) return listOf(emptyList())
    return list.flatMap { i ->
        perm(list - i).map { listOf(i) + it }
    }
}

fun main() {
    println(perm(listOf(1, 2, 3)))
}