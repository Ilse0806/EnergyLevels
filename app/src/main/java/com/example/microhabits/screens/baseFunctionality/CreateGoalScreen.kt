package com.example.microhabits.screens.baseFunctionality

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateBehavior
import com.example.microhabits.DisplayGoal
import com.example.microhabits.Home
import com.example.microhabits.components.DatePicker
import com.example.microhabits.components.Description
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.components.buttons.ReturnButton
import com.example.microhabits.components.inputs.SingleDropdown
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.overlays.SuccessOverlay
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.Goal
import com.example.microhabits.services.CreateGoalService.saveNewGoal
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getTextFieldColor
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
                        val goalString =
                            JSONObject(mapOf("id" to VariableModel.goal.value?.id)).toString()
                        navController.navigate(route = DisplayGoal(goalString))
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
            GoalCreator({ valid ->
                VariableModel.validGoal.value = valid
            })
            Spacer(Modifier.weight(1f))
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.validGoal.value,
                {
                    showSuccessOverlay = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCreator(onFormValidChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    var viewCategory by remember { mutableStateOf(false) }
    var viewOthers by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("")}

    LaunchedEffect(viewOthers, VariableModel.goal.value?.name, VariableModel.goal.value?.category) {
        if (viewOthers) {
            val isValid = !VariableModel.goal.value?.name.isNullOrEmpty() && !VariableModel.goal.value?.category.isNullOrEmpty()
            onFormValidChanged(isValid)
        } else {
            onFormValidChanged(false)
        }
    }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = name,
            label = { Text("Your goal") },
            placeholder = { Text("Eat more fruit") },
            onValueChange = { newText ->
                name = newText
                VariableModel.goal.value?.let {
                    it.name = newText
                } ?: run {
                    VariableModel.goal.value = Goal(
                        name = newText,
                        id = null,
                        description = null,
                        deadline = null,
                        category = null
                    )
                }
            },
            colors = getTextFieldColor(Color.White, C.LightBlue, Color.White),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!VariableModel.goal.value?.name.isNullOrBlank()) {
                        viewCategory = true
                    }
                }
            )
        )

        if (viewCategory) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = VariableModel.goal.value?.category.orEmpty(),
                    onValueChange = { VariableModel.goal.value?.category = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            fieldSize = coordinates.size.toSize()
                        },
                    label = { Text("Select a category") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.KeyboardArrowDown, "contentDescription",
                            Modifier.clickable { expanded = !expanded }
                        )
                    },
                    colors = getTextFieldColor(Color.White, C.LightBlue, Color.White),
                    textStyle = Typography.bodyMedium,
                    readOnly = true
                )

                SingleDropdown(
                    C.LightBlue,
                    listOf("Food", "Exercise"),
                    expanded,
                    VariableModel.goal.value?.category.orEmpty(),
                    { newSelection -> VariableModel.goal.value?.category = newSelection },
                    { newVal ->
                        expanded = newVal
                        if (!VariableModel.goal.value?.category.isNullOrBlank()) {
                            viewOthers = true
                        }
                    },
                    modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() })
                )
            }
            if (viewOthers) {
                Description(
                    VariableModel.goal.value?.description.orEmpty(),
                    onChange = { newDesc ->
                        VariableModel.goal.value?.description = newDesc
                    },
                    color = C.LightBlue
                )

                DatePicker(color = C.LightBlue)
            }
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
                GoalCreator({
                        valid -> VariableModel.validGoal.value = valid
                })
                Spacer(Modifier.weight(1f))
                ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.validGoal.value, {
                    val newGoalString = JSONObject("new goal").toString()
                    navController.navigate(route = CreateBehavior)
                })
            }
        }
    }
}