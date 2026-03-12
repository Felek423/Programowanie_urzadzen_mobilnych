fun isPerfectLoop(number: Int): String {
    var sum = 0
    for (i in 1..number / 2) {
        if (number % i == 0) {
            sum += i
        }
    }

    return when {
        sum == number -> "perfect"
        sum > number -> "abundant"
        else -> "deficient"
    }
}
fun main() {
    val testNumbers = listOf(6, 12, 8, 28, 15)



    for (num in testNumbers) {
        val result = isPerfectLoop(num)
        println("Liczba $num jest: $result")
    }
}