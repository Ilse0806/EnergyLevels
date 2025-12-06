package com.example.microhabits.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microhabits.data.state.VariableModel
import java.time.LocalDateTime
import com.example.microhabits.ui.theme.ButtonColors as ButtonC

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodFavorite (navController: NavController, modifier: Modifier = Modifier) {
    val currentHour = LocalDateTime.now().hour
    val title = when (currentHour) {
        in 4 .. 10 -> "Breakfast favorites:"
        in 11 .. 14 -> "Lunch favorites:"
        in 16 .. 21 -> "Dinner favorites:"
        else -> "Snack favorites:"
    }

    FavoritesContent(
        title = title,
        items = VariableModel.favoriteFoods.map { item ->
            item.toNavigationOption()
        },
//        items = listOf(
//            NavigationOption("Go for a walk",ExerciseDetails(1), Icons.AutoMirrored.Filled.DirectionsWalk,),
//            NavigationOption("Go for a walk", ExerciseDetails(2), Icons.AutoMirrored.Filled.DirectionsWalk),
//            NavigationOption("Go for a walk", ExerciseDetails(3), Icons.AutoMirrored.Filled.DirectionsWalk),
//            NavigationOption("Go for a walk", ExerciseDetails(4), Icons.AutoMirrored.Filled.DirectionsWalk),
//        ),
        buttonColor = ButtonC.GoldenAmberPrimary,
        textColor = Color.White,
        navController = navController,
        modifier = modifier
            .size(150.dp),
        iconColor = Color.White
    )
}