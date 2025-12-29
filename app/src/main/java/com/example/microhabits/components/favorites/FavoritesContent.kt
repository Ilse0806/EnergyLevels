package com.example.microhabits.components.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microhabits.components.navigation.InPageNavigation
import com.example.microhabits.models.classes.NavigationOption
import com.example.microhabits.ui.theme.Typography

@Composable
fun FavoritesContent(
    title: String,
    items: List<NavigationOption<*>>,
    buttonColor: ButtonColors,
    textColor: Color,
    navController: NavController,
    modifier: Modifier = Modifier,
    iconColor: Color = Color.White,
    favoriteIconDisabled: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!favoriteIconDisabled) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorites icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.padding(4.dp))
            }
            Text(
                text = title,
                style = Typography.titleMedium,
            )
        }
        InPageNavigation(
            title = null,
            navigationOptions = items,
            buttonColors = buttonColor,
            textColor = textColor,
            navController = navController,
            modifier = modifier,
            iconColor = iconColor,
            scrollable = true,
        )
    }
}