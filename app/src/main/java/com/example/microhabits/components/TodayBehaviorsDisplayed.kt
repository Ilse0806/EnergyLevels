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
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.UserGoal
import com.example.microhabits.ui.theme.Color as C

@Composable
fun TodayGoalsDisplayed(
    goals: MutableList<UserGoal>,
    onCheck: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        for (goal in goals) {
            key(goal.id) {
                var isChecked by remember { mutableStateOf(VariableModel.completedGoals.any { it.goalId == goal.goalId }) }
                Spacer(modifier = Modifier.padding(4.dp))

                fun onCheckBehavior(newChecked: Boolean) {
                    if (newChecked) {
                        onCheck(goal.id)
                    } else {
                        VariableModel.completedGoals.remove(VariableModel.completedGoals.find { it.goalId == goal.goalId })
                    }
                    isChecked = newChecked
                }

                Checkbox(Color.White, C.Indigo, isChecked, ::onCheckBehavior, goal.name)

            }
        }
    }
}