package com.example.microhabits.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microhabits.CreateGoal
import com.example.microhabits.components.buttons.Checkbox
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.deleteLater.UserGoal
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayGoalsDisplayed(
    goals: MutableList<UserGoal>,
    navController: NavController,
    onCheck: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
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
        Box (
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    navController.navigate(CreateGoal) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                .background(C.GoldenAmber),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create new goal",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}