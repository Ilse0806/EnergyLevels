package com.example.microhabits.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CollapseContent(
    expanded: Boolean,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ){
        content()
    }
}