import kotlin.math.pow

fun checkArmstrong(number: Int): Boolean {
    val numStr = number.toString()
    val power = numStr.length

    val sum = numStr.sumOf { char ->
        char.digitToInt().toDouble().pow(power).toInt()
    }
    return sum == number
}

fun main() {
    val number: Int = 153
    println(checkArmstrong(number))

    println(checkArmstrong(9))
    println(checkArmstrong(154))  
}