package com.example.microhabits

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.components.InformationDetails
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

val connectedBehaviors = mutableStateListOf<Map<String, Any?>>()
val detailsBehaviors = mutableStateListOf<Map<String, Any?>>()
val combinedBehaviors = mutableStateListOf<Map<String, Any?>>()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayGoalScreen(navController: NavController, goalMap: DisplayGoal) {
    val goal = JSONObject(goalMap.goal)
    val goalId = goal.getInt("id")
    val context = LocalContext.current

    connectedBehaviors.clear()
    detailsBehaviors.clear()
    combinedBehaviors.clear()

    val behaviors = mutableListOf<Int>()
    DatabaseService.getRow(
        "user_behavior", mapOf("goal_id" to goalId, "fetch_one" to false), context,
        { detailsBehaviorResponse ->
            val rows = detailsBehaviorResponse.getJSONArray("rows")
            for (i in 0 until rows.length()) {
                val behaviorId = rows.getJSONObject(i)["behavior_id"] as Int
                behaviors.add(behaviorId)

                val behavior = rows.getJSONObject(i)
                val map = behavior.keys().asSequence().associateWith { key ->
                    behavior.opt(key)
                }
                val exists = detailsBehaviors.any { it["id"] == map["id"] }

                if (!exists) {
                    detailsBehaviors.add(map)
                }
            }

            DatabaseService.getRow(
                "behavior", mapOf("id" to behaviors, "fetch_one" to false), context,
                { fullBehaviorResponse ->
                    val allBehaviors = fullBehaviorResponse.getJSONArray("rows")
                    for (i in 0 until allBehaviors.length()) {
                        val r = allBehaviors.get(i) as JSONObject
                        val map = r.keys().asSequence().associateWith { key ->
                            r.opt(key)
                        }

                        val exists = connectedBehaviors.any { it["id"] == map["id"] }
                        if (!exists) {
                            connectedBehaviors.add(map)
                        }
                    }

                    val habitsById = connectedBehaviors.associateBy { it["id"] as Int }
                    detailsBehaviors.forEach { behavior ->
                        val habitId = behavior["behavior_id"] as Int
                        val habit = habitsById[habitId]
                        habit?.let {
                            val combined = mutableMapOf<String, Any?>()
                            combined.putAll(habit)
                            combined.putAll(behavior)
                            combinedBehaviors.add(combined)
                        }
                    }

                    Log.d("GOALS GOTTEN in display goal", fullBehaviorResponse.toString())
                },
                { error -> Log.e("API_ERROR", error.toString()) }
            )

            Log.d("API_SUCCESS", detailsBehaviorResponse.toString())
        },
        { error -> Log.e("API_ERROR", error.toString()) }
    )

    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = {
            Navigation(navController)
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Intro(goal)
            InformationDetails(goal)
            BehaviorsDisplayed(navController, combinedBehaviors)
            Box(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun Intro(goal: JSONObject) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 80.dp, bottomStart = 80.dp))
            .background(C.CoralRed)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = goal.getString("name"),
            style = Typography.titleLarge,
        )
    }
}

@Composable
fun BehaviorsDisplayed(navController: NavController, behaviors: MutableList<Map<String, Any?>>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        for (behavior in behaviors) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val behaviorString = JSONObject(behavior).toString()
                    navController.navigate(route = DisplayBehavior(behaviorString))
                },
                colors = ButtonC.GoldenAmberPrimary,
                border = borderStroke(C.GoldenAmber),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
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
fun DisplayGoalPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            bottomBar = { Navigation(rememberNavController()) },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Intro(JSONObject("""{"deadline":null,"description":"Get healthier by training","id":1,"name":"Get healthier"}"""))
                InformationDetails(JSONObject("""{"deadline":null,"description":"Get healthier by training","id":1,"name":"Get healthier"}"""))
            }
        }
    }
}