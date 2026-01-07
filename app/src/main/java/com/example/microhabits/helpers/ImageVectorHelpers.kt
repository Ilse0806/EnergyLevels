package com.example.microhabits.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector

fun ImageVector.getName(): String {
    return when (this) {
        Icons.AutoMirrored.Default.DirectionsWalk -> "DirectionsWalk"
        Icons.Default.FitnessCenter -> "FitnessCenter"
        else -> "DirectionsWalk"
    }
}