package com.example.microhabits.helpers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.runtime.*
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

fun Modifier.focusDashBorder(color: Color) = composed {
    var isFocused by remember { mutableStateOf(false) }

    this.onFocusChanged { isFocused = it.isFocused }
        .drawBehind {
            if (isFocused) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(-5f, 2f),
                    size = Size(
                        size.width + 10,
                        size.height - 4
                    ),
                    style = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    ),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }
        }
}
