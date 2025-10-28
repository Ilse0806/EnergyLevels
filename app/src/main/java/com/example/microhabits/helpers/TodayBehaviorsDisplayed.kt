package com.example.microhabits.helpers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.microhabits.components.Checkbox
import com.example.microhabits.ui.theme.Color as C

@Composable
fun TodayBehaviorsDisplayed(behaviors: MutableList<Map<String, Any?>>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        for (behavior in behaviors) {
            var isChecked by remember { mutableStateOf(behavior["completed_today"] == 1) }
            Spacer(modifier = Modifier.padding(4.dp))

            fun onCheckBehavior(newChecked: Boolean) {
                isChecked = newChecked
            }

            Checkbox(Color.White, C.Indigo, isChecked, ::onCheckBehavior, behavior["name"] as String)
        }
    }
}