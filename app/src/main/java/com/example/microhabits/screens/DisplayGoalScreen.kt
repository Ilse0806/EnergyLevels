package com.example.microhabits.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.DisplayBehavior
import com.example.microhabits.DisplayGoal
import com.example.microhabits.Navigation
import com.example.microhabits.components.GoalDetails
import com.example.microhabits.services.GoalService
import com.example.microhabits.models.UserBehaviorWithBehavior
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C


@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayGoalScreen(navController: NavController, goalMap: DisplayGoal) {
    val goal = JSONObject(goalMap.goal)
    val goalId = goal.getInt("id")
    val context = LocalContext.current

    VariableModel.connectedBehaviors.clear()
    VariableModel.detailsBehaviors.clear()
    VariableModel.combinedBehaviors.clear()

    GoalService.loadGoals(context, goalId)

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
            Intro(goal)
            GoalDetails(goal)
            BehaviorsDisplayed(navController, VariableModel.combinedBehaviors)
            Box(Modifier.padding(innerPadding))
        }
    }
}

// Move this to a different file later if it's also used for the behaviorDetails
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
fun BehaviorsDisplayed(navController: NavController, behaviors: MutableList<UserBehaviorWithBehavior>, modifier: Modifier = Modifier) {
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
                    val behaviorString = behavior.toJson().toString()
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
                            text = behavior.behavior.name,
                            style = Typography.bodyLarge,
                        )
                        Text(
                            text = behavior.behavior.description,
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


@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DisplayGoalPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            bottomBar = {
                if (!WindowInsets.isImeVisible) {
                    Navigation(rememberNavController())
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Intro(JSONObject("""{"deadline":null,"description":"Get healthier by training","id":1,"name":"Get healthier"}"""))
                GoalDetails(JSONObject("""{"deadline":null,"description":"Get healthier by training","id":1,"name":"Get healthier"}"""))
            }
        }
    }
}