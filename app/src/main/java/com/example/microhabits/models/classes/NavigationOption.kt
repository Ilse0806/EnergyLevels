package com.example.microhabits.models.classes

import androidx.compose.ui.graphics.vector.ImageVector

class NavigationOption<T: Any>(
    val label: String,
    val destination: T,
    val icon: ImageVector? = null,
    val image: String? = null,
    val bottomNavItem: Boolean = false,
) {
}