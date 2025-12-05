package com.example.microhabits.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.microhabits.components.buttons.Checkbox
import com.example.microhabits.models.classes.Behavior
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.ui.theme.Color as C

@Composable
fun TodayBehaviorsDisplayed(behaviors: MutableList<Behavior>, modifier: Modifier = Modifier, onCheck: (Boolean, Int) -> Unit) {
    Column(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
    ) {
        for (behavior in behaviors) {
            key(behavior.id) {
                var isChecked by remember { mutableStateOf(behavior.completedToday) }
                Spacer(modifier = Modifier.padding(4.dp))

                fun onCheckBehavior(newChecked: Boolean) {
                    isChecked = newChecked
                    val index = VariableModel.todayBehaviors.indexOfFirst { it.id == behavior.id }
                    onCheck(isChecked, index)
                }

                Checkbox(Color.White, C.Indigo, isChecked, ::onCheckBehavior, behavior.name as String)

            }
        }
    }
}