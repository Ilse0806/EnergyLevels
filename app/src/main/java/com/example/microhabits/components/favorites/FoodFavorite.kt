package com.example.microhabits.components.favorites

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.NavigationOption
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodFavorite (
    buttonColor: ButtonColors,
    textColor: Color,
    navController: NavController,
    modifier: Modifier = Modifier,
    iconColor: Color = Color.White,
) {
    val currentHour = LocalDateTime.now().hour
    var title: String
    var items: List<NavigationOption<*>>
    when (currentHour) {
        in 4 .. 10 -> {
            title = "Breakfast favorites:"
            items = VariableModel.favoriteBreakfast.map { item -> item.toNavigationOption() }
        }
        in 11 .. 14 -> {
            title = "Lunch favorites:"
            items = VariableModel.favoriteLunch.map { item -> item.toNavigationOption() }
        }
        in 16 .. 21 -> {
            title = "Dinner favorites:"
            items = VariableModel.favoriteDinner.map { item -> item.toNavigationOption() }
        }
        else -> {
            title = "Snack favorites:"
            items = VariableModel.favoriteSnack.map { item -> item.toNavigationOption() }
        }
    }

    FavoritesContent(
        title = title,
        items = items,
        buttonColor = buttonColor,
        textColor = textColor,
        navController = navController,
        modifier = modifier,
        iconColor = iconColor
    )
}