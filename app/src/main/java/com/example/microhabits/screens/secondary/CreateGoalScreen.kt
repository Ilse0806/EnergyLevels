package com.example.microhabits.screens.secondary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateBehavior
import com.example.microhabits.DisplayGoal
import com.example.microhabits.Home
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.components.inputs.GoalEditor
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.overlays.SuccessOverlay
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.Goal
import com.example.microhabits.services.CreatorService.saveNewGoal
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateGoalScreen (navController: NavController) {
    VariableModel.goal.value = null
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showSuccessOverlay by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (!showSuccessOverlay) {
                Header(
                    title = "New goal",
                    titleStyle = Typography.titleLarge.copy(color = Color.White),
                    context = context,
                    headerBackground = C.LightBlue
                )
            }
        },
        bottomBar = {
            if (!WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        if (showSuccessOverlay) {
            var show by remember { mutableStateOf(false) }
            val map = mapOf(
                "name" to VariableModel.goal.value?.name,
                "description" to VariableModel.goal.value?.description,
                "deadline" to VariableModel.goal.value?.deadline,
                "category" to VariableModel.goal.value?.category,
                "new_item" to true
            )
            saveNewGoal(map, context)

            LaunchedEffect(VariableModel.goal.value?.id) {
                show = true
            }

            if (show) {
                SuccessOverlay(
                    onGoHome = { navController.navigate(route = Home) },
                    onViewGoal = {
                        navController.navigate(route = DisplayGoal(VariableModel.goal.value?.id as Int))
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f),
                    onDismiss = { navController.navigate(route = Home) },
                )
            }
        }
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Spacer(Modifier.padding(12.dp))

//            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed, {
//                navController.navigate(route = Home)
//            })
            Text(
                text = "What do you want to achieve?",
                style = Typography.titleMedium
            )
            GoalEditor(
                onFormValidChanged = { valid ->
                    VariableModel.validGoal.value = valid
                },
                onNameChange = { name ->
                    VariableModel.goal.value?.let {
                        VariableModel.goal.value!!.name = name
                    } ?: run {
                        VariableModel.goal.value = Goal(
                            name = name,
                            id = null,
                            description = null,
                            deadline = null,
                            category = null
                        )
                    }
                },
                onDescriptionChange = { description ->
                    VariableModel.goal.value?.description = description
                },
                onCategoryChange = { category ->
                    VariableModel.goal.value?.category = category
                },
                onDeadlineChange = { deadline ->
                    VariableModel.goal.value?.deadline = deadline
                },
                goal = VariableModel.goal.value,
            )
            Spacer(Modifier.weight(1f))
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.validGoal.value,
                {
                    showSuccessOverlay = true
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CreateGoalPreview() {
    val navController = rememberNavController()
    val context = LocalContext.current
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            topBar = {
                Header(
                    title = "New goal",
                    titleStyle = Typography.titleLarge.copy(color = Color.White),
                    context = context,
                    headerBackground = C.LightBlue
                )
            },
            bottomBar = {
                if (!WindowInsets.isImeVisible) {
                    Navigation(navController)
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
                Spacer(Modifier.padding(12.dp))
//                ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
//                    navController.navigate(route = Home)
//                })
//                Text(
//                    text = "Creating a new goal",
//                    style = Typography.titleLarge
//                )
                Text(
                    text = "What do you want to achieve?",
                    style = Typography.titleMedium
                )
                GoalEditor(
                    onFormValidChanged = { valid ->
                        VariableModel.validGoal.value = valid
                    },
                    onNameChange = { name ->
                        VariableModel.goal.value?.let {
                            VariableModel.goal.value!!.name = name
                        } ?: run {
                            VariableModel.goal.value = Goal(
                                name = name,
                                id = null,
                                description = null,
                                deadline = null,
                                category = null
                            )
                        }
                    },
                    onDescriptionChange = { description ->
                        VariableModel.goal.value?.description = description
                    },
                    onCategoryChange = { category ->
                        VariableModel.goal.value?.category = category
                    },
                    onDeadlineChange = { deadline ->
                        VariableModel.goal.value?.deadline = deadline
                    },
                    goal = VariableModel.goal.value,
                )
                Spacer(Modifier.weight(1f))
                ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.validGoal.value, {
                    val newGoalString = JSONObject("new goal").toString()
                    navController.navigate(route = CreateBehavior)
                })
            }
        }
    }
}