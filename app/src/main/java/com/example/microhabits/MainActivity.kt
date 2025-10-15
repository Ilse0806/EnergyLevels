package com.example.microhabits

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.components.ButtonPrimary
import com.example.microhabits.components.ButtonSecondary
import com.example.microhabits.ui.theme.Button
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONArray
import org.json.JSONObject
import com.example.microhabits.ui.theme.Color as C

var habit by mutableStateOf("")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        DatabaseService.getRow("behavior", mapOf("id" to "1"), this,
            { response ->
                checkResponse(response)
                Log.d("API_SUCCESS", response.toString()) },
            { error -> Log.e("API_ERROR", error.toString())}
        )

        setContent {
            val context = LocalContext.current
            MicroHabitsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = habit,
                        modifier = Modifier.padding(innerPadding)
                    )
                    NextButton("Click me", context)
                }
            }
        }
    }
}

fun checkResponse(results: JSONObject) {
    println("response: $results")
    habit = results["name"].toString()
}

fun buttonClicked(context: Context, intent: Intent) {
    context.startActivity(intent)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun NextButton(text: String, context: Context, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(100.dp)
    ) {
        ButtonPrimary(
            "Mauve",
            Button.MauvePrimary,
            C.Mauve,
            { buttonClicked(context, Intent(context, CreateGoal::class.java)) }
        )
    }
}

@Composable
fun Styles(modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Title",
            modifier = modifier,
            style = Typography.titleLarge,
        )
        Text(
            text = "Heading 1",
            modifier = modifier,
            style = Typography.titleMedium,
        )
        Text(
            text = "Heading 2",
            modifier = modifier,
            style = Typography.titleSmall,
        )
        Text(
            text = "Paragraph",
            modifier = modifier,
            style = Typography.bodyLarge,
        )
        Text(
            text = "Button",
            modifier = modifier,
            style = Typography.bodyMedium,
        )
        Text(
            text = "Caption",
            modifier = modifier,
            style = Typography.labelSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    MicroHabitsTheme {
        Styles()
        NextButton("Click Me", context)
    }
}