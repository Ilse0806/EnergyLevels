package com.example.microhabits.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ReturnButton(colorB: ButtonColors, color: Color,onClickAction: () -> Unit, modifier: Modifier = Modifier) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        ButtonPrimary(
            buttonColor = colorB,
            color = color,
            onClickAction = onClickAction,
            shape = RoundedCornerShape(24.dp),
            content = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "Continue"
                )
            }
        )
    }
}