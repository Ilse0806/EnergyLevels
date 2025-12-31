package com.example.microhabits.screens.baseFunctionality

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.DisplayBehavior
import com.example.microhabits.DisplayGoal
import com.example.microhabits.Home
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.components.inputs.GoalEditor
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.overlays.ErrorOverlay
import com.example.microhabits.components.overlays.SuccessOverlay
import com.example.microhabits.models.deleteLater.UserBehaviorWithBehavior
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.deleteLater.UserGoal
import com.example.microhabits.screens.main.HomeScreen
import com.example.microhabits.services.CreateGoalService.updateGoal
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C


@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayGoalScreen(navController: NavController, goal: DisplayGoal) {
    val goalId = goal.goalId
    val fullGoal = VariableModel.userGoals.find { goalId == it.goalId }
    val goalIndex = VariableModel.userGoals.indexOfFirst { it.goalId == goalId }
    val context = LocalContext.current

    var showSuccessOverlay by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            if (!showSuccessOverlay) {
                Header(
                    title = "Your goal",
                    titleStyle = Typography.titleLarge.copy(color = Color.White),
                    context = context,
                    headerBackground = C.CoralRed
                )
            }
        },
        bottomBar = {
            if (!showSuccessOverlay && !WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        if (showSuccessOverlay) {
            SuccessOverlay(
                onGoHome = { navController.navigate(route = Home) },
                onViewGoal = {},
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                onDismiss = { navController.navigate(route = Home) },
            )
        }
        fullGoal?.let {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
                    .padding(innerPadding)
            ) {
                Spacer(Modifier.padding(12.dp))
                GoalEditor(
                    onFormValidChanged = {},
                    displayAll = true,
                    onNameChange = { name ->
                        fullGoal.name = name
                        VariableModel.userGoals[goalIndex] = fullGoal
                    },
                    onDescriptionChange = { description ->
                        fullGoal.description = description
                        VariableModel.userGoals[goalIndex] = fullGoal
                    },
                    onCategoryChange = { category ->
                        fullGoal.category = category
                        VariableModel.userGoals[goalIndex] = fullGoal
                    },
                    onDeadlineChange = { deadline ->
                        fullGoal.deadline = deadline
                        VariableModel.userGoals[goalIndex] = fullGoal
                    },
                    goal = fullGoal.toGoal(),
                )
                ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                    {
                        updateGoal(VariableModel.userGoals[goalIndex].toMap(), context)
                        showSuccessOverlay = true
                    }
                )
            }
        } ?: run {
            ErrorOverlay()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DisplayGoalPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            bottomBar = {
                if (!WindowInsets.isImeVisible) {
                    Navigation(rememberNavController())
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(innerPadding)
            ) {
//                Intro(JSONObject("""{"deadline":null,"description":"Get healthier by training","id":1,"name":"Get healthier"}"""))
//                GoalDetails(JSONObject("""{"deadline":null,"description":"Get healthier by training","id":1,"name":"Get healthier"}"""))
            }
        }
    }
}