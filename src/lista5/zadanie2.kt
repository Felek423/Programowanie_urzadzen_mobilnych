import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CounterExample() {
    var counter by rememberSaveable { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(0.3f))

        // Wyświetlanie aktualnego stanu licznika
        Text(
            text = counter.toString(),
            fontSize = 250.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        // Sekcja przycisków na dole ekranu
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = { counter++ }
            ) {
                Text(text = "Count UP")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = { counter-- }
            ) {
                Text(text = "Count DOWN")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = { counter = 0 }
            ) {
                Text(text = "RESET")
            }
        }
    }
}