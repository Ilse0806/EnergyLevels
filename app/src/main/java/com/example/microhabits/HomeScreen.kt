package com.example.microhabits

import android.os.Build
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.components.ButtonPrimary
import com.example.microhabits.helpers.TodayBehaviorsDisplayed
import com.example.microhabits.models.HomeViewModel
import com.example.microhabits.models.HomeViewModel.saveGoals
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) { HomeViewModel.loadUser(context) }

    if (VariableModel.userId != 0) {
        LaunchedEffect(VariableModel.userId) {
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
                name = VariableModel.userName
            )
            TodayBehaviorsDisplayed(VariableModel.todayBehaviors)
            if (!VariableModel.userGoals.isEmpty()) {
                GoalsDisplay(navController)
            }
        }
    }
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
        modifier = modifier.padding(top = 16.dp)
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
fun NewGoalButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ButtonPrimary(
        ButtonC.CoralRedPrimary,
        C.LightBlue,
        { onClick() },
        modifier = modifier
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GoalsDisplay(navController: NavController, modifier: Modifier = Modifier) {
    LazyRow {
        items(VariableModel.userGoals) { userGoal ->
            Button (
                modifier = modifier
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
            NewGoalButton({
                navController.navigate(route = CreateGoal)
            })
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