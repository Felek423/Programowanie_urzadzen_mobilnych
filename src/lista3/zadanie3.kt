package lista3

fun suma(a: List<Int>): Int =
    a.filter { it > 0 }.sum()

fun main() {
    println(suma(listOf(1, -4, 12, 0, -3, 29, -150)))
}