package com.example.microhabits.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.microhabits.helpers.focusDashBorder
import com.example.microhabits.ui.theme.Typography

@Composable
fun ButtonSecondary(
    text: String,
    buttonColor: ButtonColors,
    color: Color,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton (
        onClick = onClickAction,
        colors = buttonColor,
        modifier = modifier.focusDashBorder(color),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = text,
            modifier = modifier,
            style = Typography.bodyMedium,
        )
    }
}