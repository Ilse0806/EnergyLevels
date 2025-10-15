package com.example.microhabits.archive

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

//
//@Composable
//fun ButtonSecondary (
//    text: String,
//    buttonColor: ButtonColors,
//    color: Color,
//    onClickAction: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    var isFocused by remember { mutableStateOf(false) }
//
//    OutlinedButton(
//        onClick = { onClickAction() },
//        colors = buttonColor,
//        modifier = modifier.onFocusChanged { isFocused = it.isFocused }
//            .drawBehind {
//                if (isFocused) {
//                    drawRoundRect(
//                        color = color,
//                        topLeft = Offset(-5f, 2f),
//                        size = Size(
//                            size.width + 10,
//                            size.height - 4
//                        ),
//                        style = Stroke(
//                            width = 4f,
//                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
//                        ),
//                        cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
//                    )
//                }
//            }
//    ) {
//        Text(text)
//    }
//}

//
//@Composable
//fun ButtonPrimary(
//    text: String,
//    buttonColor: ButtonColors,
//    color: Color,
//    onClickAction: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    var isFocused by remember { mutableStateOf(false) }
//
//    Button(
//        onClick = { onClickAction() },
//        colors = buttonColor,
//        modifier = modifier.onFocusChanged { isFocused = it.isFocused }
//            .drawBehind {
//                if (isFocused) {
//                    drawRoundRect(
//                        color = color,
//                        topLeft = Offset(-5f, 2f),
//                        size = Size(
//                            size.width + 10,
//                            size.height - 4
//                        ),
//                        style = Stroke(
//                            width = 4f,
//                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
//                        ),
//                        cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
//                    )
//                }
//            }
//    ) {
//        Text(text)
//    }
//}