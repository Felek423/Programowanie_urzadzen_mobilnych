fun countVowels(word: String ): Int{
    val vowels = setOf('a', 'e', 'i', 'o', 'u', 'y')
    return word.lowercase().count {
        char -> char in vowels
    }
}
fun main(){
    val word = "Programowanie"
    println(countVowels(word))
}