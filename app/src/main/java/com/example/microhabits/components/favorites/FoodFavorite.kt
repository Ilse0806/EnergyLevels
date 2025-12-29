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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodFavorite (
    buttonColor: ButtonColors,
    navController: NavController,
    modifier: Modifier = Modifier,
    iconColor: Color = Color.White,
    title: String? = null,
    items: List<NavigationOption<*>>? = null,
) {
    var newTitle = ""
    var newItems: List<NavigationOption<*>> = listOf()
    if (items.isNullOrEmpty() && title.isNullOrBlank()) {
        val currentHour = LocalDateTime.now().hour
        when (currentHour) {
            in 4..10 -> {
                newTitle = "Breakfast favorites:"
                newItems = VariableModel.favoriteBreakfast.map { item -> item.toNavigationOption() }
            }

            in 11..14 -> {
                newTitle = "Lunch favorites:"
                newItems = VariableModel.favoriteLunch.map { item -> item.toNavigationOption() }
            }

            in 16..21 -> {
                newTitle = "Dinner favorites:"
                newItems = VariableModel.favoriteDinner.map { item -> item.toNavigationOption() }
            }

            else -> {
                newTitle = "Snack favorites:"
                newItems = VariableModel.favoriteSnack.map { item -> item.toNavigationOption() }
            }
        }
    } else {
        if (title != null) {
            newTitle = title
        }
        if (items != null) {
            newItems = items
        }
    }

    FavoritesContent(
        title = newTitle,
        items = newItems,
        buttonColor = buttonColor,
        textColor = Color.Black,
        navController = navController,
        modifier = modifier,
        iconColor = iconColor
    )
}