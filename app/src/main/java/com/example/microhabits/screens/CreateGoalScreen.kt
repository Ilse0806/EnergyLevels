package com.example.microhabits.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateBehavior
import com.example.microhabits.Home
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.components.buttons.ReturnButton
import com.example.microhabits.components.inputs.SingleDropdown
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.services.CreateGoalService.loadCategory
import com.example.microhabits.services.CreateGoalService.saveCategory
import com.example.microhabits.data.state.VariableModel
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
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = {
            if (!WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed, {
                navController.navigate(route = Home)
            })
            Text(
                text = "Creating a new goal",
                style = Typography.titleLarge
            )
            Text(
                text = "What do you want to achieve?",
                style = Typography.titleMedium
            )
            GoalCreator(context, {
                valid -> VariableModel.validGoal.value = valid
            })
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.validGoal.value,
                {
                    saveCategory(context)
                    navController.navigate(route = CreateBehavior)
                }
            )
        }
    }
}

@Composable
fun GoalCreator(context: Context, onFormValidChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    LaunchedEffect(Unit){ loadCategory(context) }

    LaunchedEffect(VariableModel.goal.value, VariableModel.categoryValue.value) {
        val isValid = VariableModel.goal.value.isNotBlank() && VariableModel.categoryValue.value.isNotBlank()
        onFormValidChanged(isValid)
    }

    val keys = VariableModel.existingCategories.value.keys().asSequence().toList()

    Column(
        modifier = modifier.padding(top = 48.dp, bottom = 48.dp)
    ) {
        OutlinedTextField(
            value = VariableModel.goal.value,
            label = { Text("Your goal") },
            placeholder = { Text("Eat more fruit...") },
            onValueChange = { newText ->
                VariableModel.goal.value = newText
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = getTextFieldColor(Color.White, C.LightBlue, Color.White),
            textStyle = Typography.bodyMedium
        )

        OutlinedTextField(
            value = VariableModel.categoryValue.value,
            onValueChange = { VariableModel.categoryValue.value = it },
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
            textStyle = Typography.bodyMedium
        )

        val values = keys.map { key ->
            VariableModel.existingCategories.value.getString(key)
        }

        SingleDropdown(
            C.LightBlue,
            values,
            expanded,
            VariableModel.categoryValue.value,
            { newSelection -> VariableModel.categoryValue.value = newSelection },
            { newVal -> expanded = newVal },
            modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() })
        )
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
            bottomBar = {
                if (!WindowInsets.isImeVisible) {
                    Navigation(navController)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                    navController.navigate(route = Home)
                })
                Text(
                    text = "Creating a new goal",
                    style = Typography.titleLarge
                )
                Text(
                    text = "What do you want to achieve?",
                    style = Typography.titleMedium
                )
//                GoalCreator(context)
                ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.validGoal.value, {
                    val newGoalString = JSONObject("new goal").toString()
                    navController.navigate(route = CreateBehavior)
                })
            }
        }
    }
}