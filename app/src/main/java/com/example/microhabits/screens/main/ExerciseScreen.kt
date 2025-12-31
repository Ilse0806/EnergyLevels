package com.example.microhabits.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.ExerciseDetails
import com.example.microhabits.components.DetailsBlock
import com.example.microhabits.components.TodayGoalsDisplayed
import com.example.microhabits.components.favorites.FavoritesContent
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.recommended.DisplayRecommendedItems
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.CompletedGoal
import com.example.microhabits.models.classes.ExerciseProgram
import com.example.microhabits.models.classes.UserGoal
import com.example.microhabits.ui.theme.Typography
import java.time.LocalDateTime
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Header(
                title = "Exercise",
                titleStyle = Typography.titleLarge.copy(color = Color.White),
                context = context,
                headerBackground = C.CoralRed
            )
        },
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
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Spacer(Modifier.padding(12.dp))
            Text(
                text = "Your daily goals:",
                style = Typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            TodayGoalsDisplayed(
                navController = navController,
                goals = VariableModel.userGoals.filter { it.category == "Exercise" } as MutableList<UserGoal>,
                modifier = Modifier.padding(bottom = 16.dp),
                onCheck = { id ->
                    VariableModel.completedGoals.add(CompletedGoal(id, LocalDateTime.now()))
                }
            )
            FavoritesContent(
                title = "Favorite exercises:",
                items = VariableModel.favoriteExercises.map { item -> item.toNavigationOption() },
                buttonColor = ButtonC.RedPrimary,
                textColor = Color.White,
                navController = navController,
                iconColor = C.CoralRed,
                modifier = Modifier.size(150.dp)
            )
            DisplayRecommendedItems(
                title = "Recommended exercises:",
                list = VariableModel.recommendedExercise,
                context = context,
                iconProvider = { (it as ExerciseProgram).iconFromString() },
                imageProvider = { null },
                onClick = { item ->
                    navController.navigate(ExerciseDetails(item.id))
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = C.CoralRed,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewExerciseScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Header(
                title = "Exercise",
                titleStyle = Typography.titleLarge.copy(color = Color.White),
                context = context,
                headerBackground = C.CoralRed
            )
        },
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
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Spacer(Modifier.padding(12.dp))
            TodayGoalsDisplayed(
                navController = navController,
                goals = VariableModel.userGoals.filter { it.category == "Exercise" } as MutableList<UserGoal>,
                modifier = Modifier.padding(vertical = 8.dp),
                onCheck = { id ->
                    VariableModel.completedGoals.add(CompletedGoal(id, LocalDateTime.now()))
                }
            )
            DetailsBlock(
                title = "Run 4 km",
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                minutes = 45,
                difficulty = 3,
                attributes = listOf("Cardio", "Endurance"),
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = C.CoralRed,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        navController.navigate(ExerciseDetails(1))
                    },
                context = context,
            )
        }
    }
}