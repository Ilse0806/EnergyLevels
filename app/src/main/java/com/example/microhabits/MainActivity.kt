package com.example.microhabits

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.components.ButtonPrimary
import com.example.microhabits.ui.theme.Button as ButtonC
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.microhabits.ui.theme.Color as C

var user by mutableStateOf("")
var userId: Int = 0
val currentBehaviors = mutableStateListOf<Map<String, Any?>>()

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        DatabaseService.getRow("user", mapOf("id" to "1", "fetch_one" to true), this,
            { response ->
                checkResponse(this, response)
                Log.d("API_SUCCESS", response.toString()) },
            { error -> Log.e("API_ERROR", error.toString())}
        )

        setContent {
            val context = LocalContext.current
            MicroHabitsTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
                    Column (
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    ) {
                        Greeting(
                            name = user,
                            modifier = Modifier.padding(innerPadding)
                        )
                        BehaviorsDisplayed(context, currentBehaviors)
                        NextButton("Create new behavior", context)
                    }
                }
            }
        }
    }
}

fun checkResponse(context: Context, results: JSONObject) {
    user = results["first_name"].toString()
    userId = results["id"] as Int

    connectBehaviors(context, userId)
}

fun connectBehaviors(context: Context, id: Int) {
    DatabaseService.getRow("user_behavior", mapOf("user_id" to id, "fetch_one" to false), context,
        { response ->
            saveBehavior(context, response["rows"] as JSONArray)
            Log.d("API_SUCCESS", response.toString()) },
        { error -> Log.e("API_ERROR", error.toString())}
    )
}

fun saveBehavior(context: Context, results: JSONArray) {
    for (i in 0 until results.length()) {
        var behavior = results.get(i) as JSONObject
        DatabaseService.getRow("behavior", mapOf("id" to behavior["behavior_id"], "fetch_one" to true), context,
            { response ->
                behavior = response
                val map = behavior.keys().asSequence().associateWith { key ->
                    behavior.opt(key)
                }
                currentBehaviors.add(map)
                println(currentBehaviors)
                Log.d("API_SUCCESS", response.toString()) },
            { error -> Log.e("API_ERROR", error.toString())}
        )
    }
}

fun buttonClicked(context: Context, intent: Intent) {
    context.startActivity(intent)
}

fun behaviorDetails(context: Context, intent: Intent, id: Int, fullBehavior: Map<String, Any?>) {
    intent.putExtra("behavior_id", id)
    intent.putExtra("behavior", JSONObject(fullBehavior).toString())
    context.startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val dateTime = LocalDateTime.now()
    val hour = dateTime.format(DateTimeFormatter.ofPattern("HH"))

    val greeting = if (hour.toInt() in 7..11) {
        "Good morning"
    } else if (hour.toInt() in 12..17) {
        "Good afternoon"
    } else if (hour.toInt() in 18..23) {
        "Good evening"
    } else if (hour.toInt() in 0..6) {
        "Good night"
    } else {
        "Good day"
    }

    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "$greeting $name",
            style = Typography.titleSmall,
        )
        Text(
            text = "How are you behaviors going?",
            style = Typography.bodyMedium
        )
    }
}

@Composable
fun NextButton(text: String, context: Context) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ButtonPrimary(
            text,
            ButtonC.IndigoPrimary,
            C.Mauve,
            { buttonClicked(context, Intent(context, CreateGoal::class.java)) },
            Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun BehaviorsDisplayed(context: Context, behaviors: MutableList<Map<String, Any?>>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        for (behavior in behaviors) {
            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = { behaviorDetails(context, Intent(context, DisplayBehavior::class.java), behavior["id"] as Int, behavior) },
                colors = ButtonC.GoldenAmberPrimary,
                border = borderStroke(C.GoldenAmber),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text(
                            text = behavior["name"].toString(),
                            style = Typography.bodyLarge,
                        )
                        Text(
                            text = behavior["description"].toString(),
                            style = Typography.labelSmall,
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Fav",
                        tint = C.Indigo,
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    MicroHabitsTheme {
        Scaffold(modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column (
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                Greeting(
                    name = user,
                    modifier = Modifier.padding(innerPadding)
                )
//                val behaviors = listOf(mapOf("name" to "Test", "id" to 1), mapOf("name" to "Retry", "description" to "Just trying some new things for today! Just trying some new things for today!", "id" to 2))
                BehaviorsDisplayed(context, currentBehaviors)
                NextButton("Create new behavior", context)
            }
        }
    }
}