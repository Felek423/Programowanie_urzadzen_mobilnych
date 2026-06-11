package com.example.zadanie1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Wywołanie aplikacji Kalkulator
                    Calculator()
                }
            }
        }
    }
}

@Composable
fun Calculator() {
    // rememberSaveable, aby przetrwały obrót ekranu
    var num1 by rememberSaveable { mutableStateOf("") }
    var num2 by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("N/A") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Pierwsze pole tekstowe
        TextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Enter first number") },
            // Wymuszenie klawiatury numerycznej
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Drugie pole tekstowe
        TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Enter second number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Rząd z przyciskami operacji matematycznych
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { calculateResult(num1, num2, "+") { result = it } }) { Text("+") }
            Button(onClick = { calculateResult(num1, num2, "-") { result = it } }) { Text("-") }
            Button(onClick = { calculateResult(num1, num2, "*") { result = it } }) { Text("*") }
            Button(onClick = { calculateResult(num1, num2, "/") { result = it } }) { Text("/") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Wyświetlanie wyniku
        Text(
            text = "Result: $result",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

// Funkcja pomocnicza do wykonywania obliczeń na liczbach całkowitych
fun calculateResult(num1Str: String, num2Str: String, operation: String, onResult: (String) -> Unit) {
    // Zamiana tekstu na liczby całkowite. Jeśli wpisano głupoty, zwróci null.
    val n1 = num1Str.toIntOrNull()
    val n2 = num2Str.toIntOrNull()

    if (n1 == null || n2 == null) {
        onResult("Błąd: Wpisz liczby całkowite")
        return
    }

    when (operation) {
        "+" -> onResult((n1 + n2).toString())
        "-" -> onResult((n1 - n2).toString())
        "*" -> onResult((n1 * n2).toString())
        "/" -> {
            if (n2 == 0) {
                onResult("Dzielenie przez 0!") // Zabezpieczenie przed błędem
            } else {
                onResult((n1 / n2).toString())
            }
        }
    }
}