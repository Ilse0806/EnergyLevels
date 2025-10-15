package com.example.microhabits.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.microhabits.helpers.focusDashBorder
import com.example.microhabits.ui.theme.Typography

@Composable
fun ButtonPrimary(
    text: String,
    buttonColor: ButtonColors,
    color: Color,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button (
        onClick = onClickAction,
        colors = buttonColor,
        modifier = modifier.focusDashBorder(color)
    ) {
        Text(
            text = text,
            modifier = modifier,
            style = Typography.bodyMedium,
        )
    }
}