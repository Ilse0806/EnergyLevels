package com.example.microhabits.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.microhabits.helpers.focusDashBorder

@Composable
fun ButtonSecondary(
    buttonColor: ButtonColors,
    color: Color,
    onClickAction: () -> Unit,
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    shape: Shape = ButtonDefaults.outlinedShape
) {
    OutlinedButton (
        onClick = onClickAction,
        colors = buttonColor,
        modifier = modifier.focusDashBorder(color),
        border = BorderStroke(1.dp, color),
        contentPadding = contentPadding,
        shape = shape
    ) {
        content()
    }
}