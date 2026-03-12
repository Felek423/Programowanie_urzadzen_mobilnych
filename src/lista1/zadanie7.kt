fun sumEven(number: Int): Int{
    return (2..number step 2).sum()
}
fun main(){
    val number: Int = 10
    println(sumEven(number))
    println(sumEven(number = 120))
}