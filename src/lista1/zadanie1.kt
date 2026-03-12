fun foo(n: Int) {
    for (i in 1..n) {
        when {
            i % 15 == 0 -> println("trzypiec")
            i % 3 == 0 -> println("trzy")
            i % 5 == 0 -> println("piec")
            else -> println(i)
        }
    }
}
fun main(){
    val num: Int = 15
    foo(num)
}


