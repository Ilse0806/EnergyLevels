package com.example.microhabits

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.components.ButtonPrimary
import com.example.microhabits.components.Checkbox
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

var user by mutableStateOf<JSONObject?>(null)
var userName by mutableStateOf("")
var userId by mutableIntStateOf(0)
val allBehaviors = mutableStateListOf<Map<String, Any?>>()
val todayBehaviors = mutableStateListOf<Map<String, Any?>>()
val userGoals = mutableStateListOf<Map<String, Any?>>()

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        DatabaseService.getRow(
            "user", mapOf("id" to "1", "fetch_one" to true), context,
            { response ->
                checkResponse(context, response)
                Log.d("API_SUCCESS", response.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }

    if (userId != 0) {
        LaunchedEffect(userId) {
            saveGoals(context)
        }
    }
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            if (!WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Greeting(
                name = userName,
                modifier = Modifier.padding(innerPadding)
            )
            TodayBehaviorsDisplayed(context, todayBehaviors)
            if (!userGoals.isEmpty()) {
                GoalsDisplay(navController)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun checkResponse(context: Context, results: JSONObject) {
    user = results
    userName = results["first_name"].toString()
    userId = results["id"] as Int

    connectBehaviors(context, userId)
}

@RequiresApi(Build.VERSION_CODES.O)
fun connectBehaviors(context: Context, id: Int) {
    DatabaseService.getRow("user_behavior", mapOf("user_id" to id, "fetch_one" to false), context,
        { response ->
            saveBehavior(context, response["rows"] as JSONArray)
            Log.d("API_SUCCESS", response.toString()) },
        { error -> Log.e("API_ERROR", error.toString())}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun saveBehavior(context: Context, results: JSONArray) {
    for (i in 0 until results.length()) {
        var behavior = results.get(i) as JSONObject
        val today = checkNotification(behavior)
        val completedToday = behavior["completed_today"]

        DatabaseService.getRow("behavior", mapOf("id" to behavior["behavior_id"], "fetch_one" to true), context,
            { response ->
                behavior = response
                val map = behavior.keys().asSequence().associateWith { key ->
                    behavior.opt(key)
                }.toMutableMap().apply {
                    put("completed_today", completedToday)
                }
                val exists = allBehaviors.any { it["id"] == map["id"] }

                if (!exists) {
                    allBehaviors.add(map)
                    if (today) {
                        todayBehaviors.add(map)
                    }
                }
                Log.d("BEHAVIOR API SUCCESFULL", response.toString()) },
            { error -> Log.e("API_ERROR", error.toString())}
        )
    }
}

fun saveGoals(context: Context) {
    val goalIds = mutableListOf<Int>()
    DatabaseService.getRow("user_goals", mapOf("user_id" to userId, "fetch_one" to false), context,
        { response ->
            val rows = response.getJSONArray("rows")
            for (i in 0 until rows.length()) {
                val goalId = rows.getJSONObject(i)["goal_id"] as Int
                goalIds.add(goalId)
            }

            DatabaseService.getRow("goal", mapOf("id" to goalIds, "fetch_one" to false), context,
                { goalResponse ->
                    val goals = goalResponse.getJSONArray("rows")
                    for (i in 0 until goals.length()) {
                        val r = goals.get(i) as JSONObject
                        val map = r.keys().asSequence().associateWith { key ->
                            r.opt(key)
                        }

                        val exists = userGoals.any { it["id"] == map["id"] }
                        if (!exists) {
                            userGoals.add(map)
                        }
                    }

                    Log.d("GOALS GOTTEN", goalResponse.toString()) },
                { error -> Log.e("API_ERROR", error.toString())}
            )
            Log.d("API_SUCCESS", response.toString()) },
        { error -> Log.e("API_ERROR", error.toString())}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun checkNotification(behavior: JSONObject): Boolean {
    val startDateStr = behavior["start_date"] as String
    val startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH))

    if (behavior["notification"] == 1) {
        val todayDate = LocalDate.now()
        val daysSinceStart = ChronoUnit.DAYS.between(startDate, todayDate)
        val interval = behavior["notification_interval"] as Int

        when (behavior["notification_frequency"]) {
            "daily" -> {
                if (daysSinceStart % interval == 0L) {
                    return true
                }
            }
            "weekly" -> {
                val notificationDayIndex = behavior["notification_day"] as? Int
                val todayDayIndex = todayDate.dayOfWeek.value % 7
                if (notificationDayIndex != null && todayDayIndex == notificationDayIndex) {
                    return true
                }
            }
            "monthly" -> {
                val notificationDayOfMonth = behavior["notification_day_of_month"] as? Int
                if (notificationDayOfMonth != null && todayDate.dayOfMonth == notificationDayOfMonth) {
                    return true
                }
            }
        }
    }
    return false
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
            style = Typography.titleLarge,
        )
        Text(
            text = "How are you behaviors going today?",
            style = Typography.bodyMedium
        )
    }
}

@Composable
fun NewGoalButton(text: String, onClick: () -> Unit) {
    ButtonPrimary(
        ButtonC.CoralRedPrimary,
        C.LightBlue,
        { onClick() },
        modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            .border(2.dp, C.CoralRed, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create new goal",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    )
}

@Composable
fun TodayBehaviorsDisplayed(context: Context, behaviors: MutableList<Map<String, Any?>>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        for (behavior in behaviors) {
            var isChecked by remember { mutableStateOf(behavior["completed_today"] == 1) }
            Spacer(modifier = Modifier.padding(4.dp))

            fun onCheckBehavior(newChecked: Boolean) {
                isChecked = newChecked
            }

            Checkbox(Color.White, C.Indigo, isChecked, ::onCheckBehavior, behavior["name"] as String)
        }
    }
}

@Composable
fun GoalsDisplay(navController: NavController, modifier: Modifier = Modifier) {
    LazyRow {
        items(userGoals) { userGoal ->
            Button (
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp),
                border = BorderStroke(2.dp, C.CoralRed),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val userGoalString = JSONObject(userGoal).toString()
                    navController.navigate(route = DisplayGoal(userGoalString))
                },
                colors = ButtonC.CoralRedSecondary.copy(
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = userGoal["name"] as String,
                    style = Typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.size(8.dp))
        }
        item {
            NewGoalButton("Create new behavior"
            ) {
                navController.navigate(route = CreateGoal)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            bottomBar = { Navigation(rememberNavController()) },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Text("hi")
            }
            GoalsDisplay(rememberNavController())
        }
    }
}