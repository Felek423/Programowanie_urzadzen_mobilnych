import kotlin.math.sqrt

fun isPrime(number: Int): Boolean {
    if (number <= 1){
        return false
    }

    val limit = sqrt(number.toDouble()).toInt()
    for (i in 2..limit){
        if (number % i == 0){
            return false
        }
    }
    return true
}
fun main(){
    val number: Int = 17
    println(isPrime(number))
    println(isPrime(number = 18))
}