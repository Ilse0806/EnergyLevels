package com.example.microhabits.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ContinueButton(colorB: ButtonColors, color: Color, enabled: Boolean, onClickAction: () -> Unit, modifier: Modifier = Modifier) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        ButtonPrimary(
            buttonColor = colorB,
            color = color,
            onClickAction = onClickAction,
            shape = RoundedCornerShape(24.dp),
            enabled = enabled,
            content = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    "Continue"
                )
            }
        )
    }
}