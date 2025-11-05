package com.example.microhabits.ui.theme

import androidx.compose.material3.SwitchColors
import androidx.compose.ui.graphics.Color

fun getSwitchColors(color: Color): SwitchColors {
    return SwitchColors(
        checkedThumbColor = Color.White,
        checkedTrackColor = color,
        checkedBorderColor = Color.Transparent,
        checkedIconColor = color,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = Color(0xFFD9D9D9),
        uncheckedBorderColor = Color.Transparent,
        uncheckedIconColor = color,
        disabledCheckedThumbColor = Color.White,
        disabledCheckedTrackColor = color.copy(alpha = 0.4f),
        disabledCheckedBorderColor = Color.Transparent,
        disabledCheckedIconColor = color,
        disabledUncheckedThumbColor = Color.White.copy(alpha = 0.7f),
        disabledUncheckedTrackColor = Color(0xFFD9D9D9).copy(alpha = 0.9f),
        disabledUncheckedBorderColor = color.copy(alpha = 0.3f),
        disabledUncheckedIconColor = color
    )
}