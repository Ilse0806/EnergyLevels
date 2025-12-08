package com.example.microhabits.components.favorites

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.microhabits.models.classes.NavigationOption
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodFavorite (
    items: List<NavigationOption<*>>,
    buttonColor: ButtonColors,
    textColor: Color,
    navController: NavController,
    modifier: Modifier = Modifier,
    iconColor: Color = Color.White,
) {
    val currentHour = LocalDateTime.now().hour
    val title = when (currentHour) {
        in 4 .. 10 -> "Breakfast favorites:"
        in 11 .. 14 -> "Lunch favorites:"
        in 16 .. 21 -> "Dinner favorites:"
        else -> "Snack favorites:"
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