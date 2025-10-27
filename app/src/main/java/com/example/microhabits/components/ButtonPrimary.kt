package com.example.microhabits.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.example.microhabits.helpers.focusDashBorder

@Composable
fun ButtonPrimary(
    buttonColor: ButtonColors,
    color: Color,
    onClickAction: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = ButtonDefaults.shape,
    enabled: Boolean = true,
    ) {
    Button (
        onClick = onClickAction,
        colors = buttonColor,
        modifier = modifier.focusDashBorder(color),
        enabled = enabled,
        shape = shape
    ) {
        content()
    }
}