package com.example.microhabits.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microhabits.components.buttons.ButtonSecondary
import com.example.microhabits.models.classes.NavigationOption
import com.example.microhabits.ui.theme.Typography

@Composable
fun InPageNavigation(
    title: String?,
    navigationOptions: List<NavigationOption<*>>,
    buttonColors: ButtonColors,
    textColor: Color,
    navController: NavController,
    modifier: Modifier = Modifier,
    iconColor: Color = Color.White,
    scrollable: Boolean = false,
) {
    val content: @Composable (NavigationOption<*>) -> Unit = { option ->
        ButtonSecondary(
            buttonColor = buttonColors,
            color = textColor,
            onClickAction = {
                navController.navigate(route = option.destination)
            },
            content = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    option.icon?.let {
                        Icon(
                            imageVector = option.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize(1.0f)
                                .align(Alignment.Center),
                            tint = iconColor,
                        )
                    }
                    Text(
                        text = option.label,
                        style = Typography.titleSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            },
            modifier = modifier,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        if (!title.isNullOrEmpty()){
            Text(
                text = title,
                style = Typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        if (scrollable) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(navigationOptions) { option ->
                    content(option)
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                navigationOptions.forEach { option ->
                    content(option)
                }
            }
        }
    }
}