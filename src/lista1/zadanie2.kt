fun isPalindrome(word: String): Boolean {
    return word == word.reversed().lowercase()
}

fun main() {
    val word = "Kajak"
    println(isPalindrome(word))
}