package com.example.microhabits.components

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.microhabits.ui.theme.Typography

@Composable
fun ButtonTertiary(
    text: String,
    buttonColor: ButtonColors,
    color: Color,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }

    TextButton(
        onClick = { onClickAction() },
        colors = buttonColor,
        modifier = modifier.onFocusChanged { isFocused = it.isFocused }
            .drawBehind {
                if (isFocused) {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(20f, 35f),
                        size = Size(
                            size.width - 40,
                            size.height - 70
                        ),
                        style = Stroke(
                            width = 4f,
                        ),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    )
                }
            }
    ) {
        Text(
            text = text,
            modifier = modifier,
            style = Typography.bodyMedium,
        )
    }
}