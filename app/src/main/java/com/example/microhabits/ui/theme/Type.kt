package com.example.microhabits.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.microhabits.R

val Typography = Typography(
//    Title
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.gabarito_t)),
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 40.sp,
    ),
//    Heading 1
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.gabarito_t)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 30.sp,
    ),
//    Heading 2
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.gabarito_t)),
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 100.sp,
    ),
//    Paragraph
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.gabarito_t)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
//    Button text
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.funnel_sans_t)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
//    Caption
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.funnel_sans_italic)),
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)