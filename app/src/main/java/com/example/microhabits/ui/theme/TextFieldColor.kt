package com.example.microhabits.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.graphics.Color


fun getTextFieldColor(borderColor: Color, innerColor: Color = Color.White, textColor: Color = Color.Black): TextFieldColors {
    return TextFieldColors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        disabledTextColor = Color.Gray,
        errorTextColor = Color.Red,
        focusedContainerColor = innerColor,
        unfocusedContainerColor = innerColor,
        disabledContainerColor = innerColor,
        errorContainerColor = innerColor,
        cursorColor = borderColor,
        errorCursorColor = Color.Red,
        textSelectionColors = TextSelectionColors(
            handleColor = borderColor,
            backgroundColor = borderColor.copy(alpha = 0.4f)
        ),
        focusedIndicatorColor = borderColor,
        unfocusedIndicatorColor = borderColor,
        disabledIndicatorColor = borderColor,
        errorIndicatorColor = Color.Red,
        focusedLeadingIconColor = borderColor,
        unfocusedLeadingIconColor = borderColor,
        disabledLeadingIconColor = borderColor.copy(alpha = 0.5f),
        errorLeadingIconColor = Color.Red,
        focusedTrailingIconColor = borderColor,
        unfocusedTrailingIconColor = borderColor,
        disabledTrailingIconColor = borderColor.copy(alpha = 0.5f),
        errorTrailingIconColor = Color.Red,
        focusedLabelColor = borderColor,
        unfocusedLabelColor = borderColor,
        disabledLabelColor = Color.Gray,
        errorLabelColor = Color.Red,
        focusedPlaceholderColor = textColor.copy(alpha = 0.6f),
        unfocusedPlaceholderColor = textColor.copy(alpha = 0.6f),
        disabledPlaceholderColor = Color.Gray,
        errorPlaceholderColor = Color.Red,
        focusedSupportingTextColor = Color.Gray,
        unfocusedSupportingTextColor = Color.Gray,
        disabledSupportingTextColor = Color.LightGray,
        errorSupportingTextColor = Color.Red,
        focusedPrefixColor = borderColor,
        unfocusedPrefixColor = borderColor,
        disabledPrefixColor = Color.Gray,
        errorPrefixColor = Color.Red,
        focusedSuffixColor = borderColor,
        unfocusedSuffixColor = borderColor,
        disabledSuffixColor = Color.Gray,
        errorSuffixColor = Color.Red
    )
}