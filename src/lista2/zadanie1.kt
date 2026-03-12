data class UserInput(val name: String?, val email: String?, val age: String?)

data class UserProfile(
    var name: String = "",
    var email: String = "",
    var age: Int = 0,
    var isAdult: Boolean = false
)

fun buildProfile(input: UserInput?, logs: MutableList<String>): UserProfile? {
    if (input == null) {
        logs.add("Input is null")
        return null
    }

    if (input.name == null) {
        logs.add("Name is null")
        return null
    }
    val name = input.name.trim()
    if (name.length < 3) {
        logs.add("Name too short")
        return null
    }

    if (input.email == null) {
        logs.add("Email is null")
        return null
    }
    val email = input.email.trim().lowercase()
    if (!email.contains("@")) {
        logs.add("Invalid email")
        return null
    }

    if (input.age == null) {
        logs.add("Age is null")
        return null
    }
    val age = input.age.toIntOrNull() ?: run {
        logs.add("Age is not a number")
        return null
    }

    return UserProfile().apply {
        this.name = name
        this.email = email
        this.age = age
        this.isAdult = age >= 18
    }.also {
        logs.add("Profile created for $email")
    }
}

fun main() {
    val logs = mutableListOf<String>()
    val profile = buildProfile(UserInput("Jan", "JAN@example.com", "25"), logs)
    println(profile)
    println("Logi: $logs")
}
