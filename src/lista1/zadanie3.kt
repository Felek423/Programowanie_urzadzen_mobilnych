fun printPascal(height: Int): String {
    val builder = StringBuilder()

    for (i in 0 until height) {
        for (space in 1 until height - i) {
            builder.append(" ")
        }

        var number = 1
        for (j in 0..i) {
            builder.append(number)

            if (j < i) {
                builder.append(" ")
            }


            number = number * (i - j) / (j + 1)
        }

        if (i < height - 1) {
            builder.append("\n")
        }
    }

    return builder.toString()
}

fun main() {
    val height: Int = 7
    println(printPascal(height))
}