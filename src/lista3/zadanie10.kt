package lista3

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun plus(value: Int) = Point(x + value, y + value)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun times(other: Point) = Point(x * other.x, y * other.y)
    operator fun inc() = Point(x + 1, y + 1)
    operator fun dec() = Point(x - 1, y - 1)
    operator fun not() = Point(-x, -y)

    override fun toString(): String = "($x, $y)"
}

fun main() {
    var p1 = Point(1, 1)
    val p2 = Point(2, 2)

    println("input: p1 + p2    output: ${p1 + p2}")

    p1 += 1
    println("input: p1 += 1    output: $p1")

    p1 = Point(1, 1)
    println("input: p1 - p2    output: ${p1 - p2}")
    println("input: p1 * p2    output: ${p1 * p2}")

    p1++
    println("input: p1++    output: $p1")

    p1 = Point(1, 1)
    p1--
    println("input: p1--    output: $p1")

    p1 = Point(1, 1)
    println("input: !p1    output: ${!p1}")
}