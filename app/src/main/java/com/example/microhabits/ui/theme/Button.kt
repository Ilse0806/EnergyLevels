package com.example.microhabits.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.ui.graphics.Color
import com.example.microhabits.ui.theme.Color as C

object Button {
    val IndigoPrimary= ButtonColors(C.Indigo, Color.White, C.Indigo.copy(alpha=0.5f), Color.White.copy(alpha=0.4f))
    val IndigoSecondary = ButtonColors(Color.Transparent, C.Indigo, Color.White.copy(alpha=0.5f), C.Indigo.copy(alpha=0.4f))

    val MauvePrimary = ButtonColors(C.Mauve, Color.Black, C.Mauve.copy(alpha=0.5f), Color.Black.copy(alpha=0.4f))
    val MauveSecondary = ButtonColors(Color.Transparent, C.Mauve, Color.White.copy(alpha=0.5f), C.Mauve.copy(alpha=0.4f))

    val GoldenAmberPrimary = ButtonColors(C.GoldenAmber, Color.Black, C.GoldenAmber.copy(alpha=0.5f), Color.Black.copy(alpha=0.4f))
    val GoldenAmberSecondary = ButtonColors(Color.Transparent, C.GoldenAmber, Color.White.copy(alpha=0.5f), C.GoldenAmber.copy(alpha=0.4f))

    val CoralRedPrimary = ButtonColors(C.CoralRed, Color.Black, C.CoralRed.copy(alpha=0.5f), Color.Black.copy(alpha=0.4f))
    val CoralRedSecondary = ButtonColors(Color.Transparent, C.CoralRed, Color.White.copy(alpha=0.5f), C.CoralRed.copy(alpha=0.4f))

    val RedPrimary = ButtonColors(C.Red, Color.White, C.Red.copy(alpha=0.5f), Color.White.copy(alpha=0.4f))
    val RedSecondary = ButtonColors(Color.Transparent, C.Red, Color.White.copy(alpha=0.5f), C.Red.copy(alpha=0.4f))
}
