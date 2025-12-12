package com.example.microhabits.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.Exercise
import com.example.microhabits.Food
import com.example.microhabits.components.TodayGoalsDisplayed
import com.example.microhabits.components.buttons.ButtonPrimary
import com.example.microhabits.components.favorites.FavoritesContent
import com.example.microhabits.components.favorites.FoodFavorite
import com.example.microhabits.components.navigation.InPageNavigation
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.CompletedGoal
import com.example.microhabits.models.classes.NavigationOption
import com.example.microhabits.services.MainService
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

var hasLoaded = false

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

//    if (!hasLoaded.value) {
//        hasLoaded.value = true
//        HomeService.loadUser(context)
//        println("runs")
//        LaunchedEffect(VariableModel.userId != 0) {
//            FavoritesService.loadFavorites(context)
//        }
//    }
    if (!hasLoaded) {
        hasLoaded = true
        MainService.loadUser(context)
    }

    if (VariableModel.userId != 0) {
        LaunchedEffect(VariableModel.userId) {
//            saveGoals(context)
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
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Greeting(
                name = VariableModel.userName
            )
            TodayGoalsDisplayed(
                goals = VariableModel.userGoals,
                modifier = Modifier.padding(vertical = 8.dp),
                onCheck = { id ->
                    VariableModel.completedGoals.add(CompletedGoal(id, LocalDateTime.now()))
                }
            )
            InPageNavigation(
                title = "What do you want to do?",
                navigationOptions = listOf(
                    NavigationOption("Exercise", Exercise),
                    NavigationOption("Food", Food)
                ),
                buttonColors = ButtonC.CoralRedSecondary.copy(containerColor = Color.White),
                textColor = C.CoralRed,
                navController = navController,
                modifier = Modifier
                    .weight(0.5f)
                    .aspectRatio(1f)
            )
            FavoritesContent(
                title = "Favorite exercises:",
                items = VariableModel.favoriteExercises.map { item ->
                    item.toNavigationOption()
                },
                buttonColor = ButtonC.RedPrimary,
                textColor = Color.White,
                navController = navController,
                modifier = Modifier
                    .size(150.dp),
                iconColor = C.CoralRed
            )
            FoodFavorite(
                buttonColor = ButtonC.GoldenAmberPrimary,
                textColor = Color.White,
                navController = navController,
                modifier = Modifier
                    .size(150.dp),
                iconColor = Color.White
            )
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

    Column {
        Text(
            text = "$greeting $name",
            style = Typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "How is your day going?",
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
                Greeting(
                    name = "Bob"
                )
                InPageNavigation(
                    title = "What do you want to do?",
                    navigationOptions = listOf(
                        NavigationOption("Exercise", Exercise),
                        NavigationOption("Food", Food)
                    ),
                    buttonColors = ButtonC.CoralRedSecondary.copy(containerColor = Color.White),
                    textColor = C.CoralRed,
                    navController = rememberNavController(),
                    modifier = Modifier
                        .weight(0.5f)
                        .aspectRatio(1f)
                        .padding(vertical = 8.dp)
                )
                FavoritesContent(
                    title = "Favorite exercises:",
                    items = listOf(
                        NavigationOption("Go for a walk", Exercise, Icons.AutoMirrored.Filled.DirectionsWalk),
                        NavigationOption("Go for a walk", Exercise, Icons.AutoMirrored.Filled.DirectionsWalk)
                    ),
                    buttonColor = ButtonC.RedPrimary,
                    textColor = Color.White,
                    navController = rememberNavController(),
                    modifier = Modifier
                        .size(150.dp),
                    iconColor = C.CoralRed
                )
                FoodFavorite(
                    buttonColor = ButtonC.GoldenAmberPrimary,
                    textColor = Color.White,
                    navController = rememberNavController(),
                    modifier = Modifier
                        .size(150.dp),
                    iconColor = Color.White
                )
            }
        }
    }
}