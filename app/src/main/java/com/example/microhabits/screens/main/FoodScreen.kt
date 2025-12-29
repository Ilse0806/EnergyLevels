package com.example.microhabits.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.ExerciseDetails
import com.example.microhabits.FoodDetails
import com.example.microhabits.components.DetailsBlock
import com.example.microhabits.components.favorites.FavoritesContent
import com.example.microhabits.components.favorites.FoodFavorite
import com.example.microhabits.components.navigation.Filtering
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.recommended.DisplayRecommendedItems
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.FilterItem
import com.example.microhabits.models.classes.FoodRecipe
import com.example.microhabits.models.classes.NavigationOption
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import java.time.LocalDateTime
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

var All = FilterItem<FoodRecipe>("All", true, favoritesList = listOf(), recommendedList = mutableStateListOf())
var Breakfast = FilterItem<FoodRecipe>("Breakfast", selected = false, singleSelect = false, VariableModel.favoriteBreakfast.map { it.toNavigationOption() }, VariableModel.recommendedBreakfast)
var Lunch = FilterItem<FoodRecipe>("Lunch", selected = false, singleSelect = false, VariableModel.favoriteLunch.map { it.toNavigationOption() }, VariableModel.recommendedLunch)
var Dinner = FilterItem<FoodRecipe>("Dinner", selected = false, singleSelect = false, VariableModel.favoriteDinner.map { it.toNavigationOption() }, VariableModel.recommendedDinner)
var Snacks = FilterItem<FoodRecipe>("Snacks", selected = false, singleSelect = false, VariableModel.favoriteSnack.map { it.toNavigationOption() }, VariableModel.recommendedSnack)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodScreen (navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Header(
                title = "Food",
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
            Filtering(listOf(All, Breakfast, Lunch, Dinner, Snacks))

            var title: String
            var items: List<NavigationOption<*>>
            var recommendedList: SnapshotStateList<FoodRecipe>
            var recommendedTitle: String
            if (All.selected) {
                when (LocalDateTime.now().hour) {
                    in 4..10 -> {
                        title = "Breakfast favorites:"
                        items = VariableModel.favoriteBreakfast.map { it.toNavigationOption() }
                        recommendedTitle = "Breakfast recommendations:"
                        recommendedList = VariableModel.recommendedBreakfast
                    }
                    in 11..14 -> {
                        title = "Lunch favorites:"
                        items = VariableModel.favoriteLunch.map { it.toNavigationOption() }
                        recommendedTitle = "Lunch recommendations:"
                        recommendedList = VariableModel.recommendedLunch
                    }
                    in 16..21 -> {
                        title = "Dinner favorites:"
                        items = VariableModel.favoriteDinner.map { it.toNavigationOption() }
                        recommendedTitle = "Dinner recommendations:"
                        recommendedList = VariableModel.recommendedDinner
                    }
                    else -> {
                        title = "Snack favorites:"
                        items = VariableModel.favoriteSnack.map { it.toNavigationOption() }
                        recommendedTitle = "Snack recommendations:"
                        recommendedList = VariableModel.recommendedSnack
                    }
                }

                FoodFavorite(
                    title = title,
                    items = items,
                    buttonColor = ButtonC.GoldenAmberPrimary,
                    navController = navController,
                    modifier = Modifier
                        .size(150.dp),
                    iconColor = Color.White
                )

                FavoritesContent(
                    title = "Snacks:",
                    items = VariableModel.recommendedSnack.map { item ->
                        item.toNavigationOption()
                    },
                    buttonColor = ButtonC.GoldenAmberPrimary,
                    textColor = Color.White,
                    navController = navController,
                    modifier = Modifier
                        .size(150.dp),
                    iconColor = Color.White,
                    favoriteIconDisabled = true
                )
                DisplayRecommendedItems(
                    title = recommendedTitle,
                    list = recommendedList,
                    context = context,
                    iconProvider = { null },
                    imageProvider = { (it as FoodRecipe).image },
                    mainColor = C.Red,
                    accentColor = C.Red,
                    onClick = { item ->
                        navController.navigate(FoodDetails(item.id))
                    },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = C.Red,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            } else {
                val list = listOf(Breakfast, Lunch, Dinner, Snacks)
                for (item in list) {
                    if (item.selected) {
                        FoodFavorite(
                            title = "${item.name} favorites:",
                            items = item.favoritesList,
                            buttonColor = ButtonC.GoldenAmberPrimary,
                            navController = navController,
                            modifier = Modifier
                                .size(150.dp),
                            iconColor = Color.White
                        )
                        DisplayRecommendedItems(
                            title = "${item.name} recommendations:",
                            list = item.recommendedList,
                            context = context,
                            iconProvider = { null },
                            imageProvider = { (it as FoodRecipe).image },
                            mainColor = C.Red,
                            accentColor = C.Red,
                            onClick = { item ->
                                navController.navigate(FoodDetails(item.id))
                            },
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = C.Red,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewFoodScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Header(
                title = "Food",
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
            Filtering(listOf(All, Breakfast, Lunch, Dinner, Snacks))
            DetailsBlock(
                title = "Run 4 km",
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                minutes = 45,
                difficulty = 3,
                attributes = listOf("Cardio", "Endurance"),
                mainColor = C.Red,
                accentColor = C.Red,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = C.Red,
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