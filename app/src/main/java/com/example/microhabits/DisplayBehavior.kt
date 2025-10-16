package com.example.microhabits

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject
import com.example.microhabits.ui.theme.Color as C
import androidx.compose.ui.graphics.Color
import java.util.concurrent.TimeUnit

var behaviorDetailList: MutableState<JSONObject?> = mutableStateOf(null)

class DisplayBehavior : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val selectedBehavior = JSONObject(intent.getStringExtra("behavior") ?: "{}")

        DatabaseService.getRow("user_behavior", mapOf("behavior_id" to intent.getIntExtra("behavior_id", 0), "fetch_one" to true), this,
            { response ->
                behaviorDetailList.value = response
                Log.d("API_SUCCESS", response.toString()) },
            { error -> Log.e("API_ERROR", error.toString())}
        )

        setContent {
            MicroHabitsTheme {
                Scaffold(
                    containerColor = C.Mauve,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
                    Column {
                        behaviorDetailList.value?.let { behavior ->
                            BehaviorDetailsShown(selectedBehavior, behaviorDetailList)
                        } ?: run {
                            Text("Loading...", Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BehaviorDetailsShown(fullBehavior: JSONObject, behaviorDetails: MutableState<JSONObject?>) {
    val detailList = defineDetails(fullBehavior, behaviorDetails)

    Column {
        Column(
            modifier = Modifier
                .background(
                    color = C.Indigo,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, bottom = 16.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = fullBehavior.get("name").toString(),
                style = Typography.titleMedium,
                color = Color.White,
            )
            if (fullBehavior.get("description").toString().isEmpty()) {
                Text(
                    text = fullBehavior.get("description").toString(),
                    style = Typography.bodyMedium,
                    color = Color.White,
                )
            }
        }

        Details(detailList)
    }
}

@Composable
fun Details(items: List<Map<String,Any>>) {
    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        Column {
            items.forEach { item ->
                Text(
                    text = "${item["title"]}: ${item["text"]}",
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}

fun defineDetails(fullBehavior: JSONObject, behaviorDetails: MutableState<JSONObject?>): List<Map<String, Any>> {
    println(behaviorDetails)
    println(fullBehavior)
    val goldenBehavior = behaviorDetails.value?.get("golden_behavior") == 1
    val notification = TimeUnit.SECONDS.toHours(
        (behaviorDetails.value?.get("notification") as Number).toLong()
    )
    val measuredIn = fullBehavior.get("measured_in").toString()

    val items = listOf(
        mapOf(
            "title" to "Notification",
            "text" to "Send me a notification every $notification hours"
        ),
        mapOf(
            "title" to "Golden Behavior",
            "text" to if (goldenBehavior) "This is one of your golden behaviors :)" else "This isn't one of your golden behaviors"
        ),
        mapOf(
            "title" to "Frequency",
            "text" to if (measuredIn == "time")
                "You should do this behavior for 30 seconds every day"
            else
                "You should do this behavior at least 1 time every day"
        )
    )

    return items
}

@Preview(showBackground = true)
@Composable
fun BehaviorPreview() {
    MicroHabitsTheme {
        Scaffold(
            containerColor = C.Mauve,
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding)
            ) {
                Column {
//                    Details(true, 1, "amount of times")
                }
            }
        }
    }
}