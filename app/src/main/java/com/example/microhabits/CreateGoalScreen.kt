package com.example.microhabits

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.components.ContinueButton
import com.example.microhabits.components.ReturnButton
import com.example.microhabits.helpers.crop
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getTextFieldColor
import org.json.JSONObject
import kotlin.toString
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

var validGoal = mutableStateOf(false)
var categoryValue = mutableStateOf("")
var goal = mutableStateOf("")
var existingCategories = mutableStateOf(JSONObject())


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
                valid -> validGoal.value = valid
            })
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, validGoal.value, {
                val allCats = existingCategories.value.keys().asSequence().toList()
                for (key in allCats) {
                    val category = existingCategories.value.getString(key)
                    if (categoryValue.value != category) {
                        DatabaseService.updateRow("category", mapOf("name" to categoryValue.value), context,
                            { savedCategory ->
                                Log.d("API_SUCCESS", savedCategory.toString())
                            },
                            { error -> Log.e("API_ERROR", error.toString()) }
                        )
                        break
                    }
                }

                val newGoalString = goal
//                val newGoalString = JSONObject("new goal")
//                navController.navigate(route = CreateBehavior("newGoalString"))
            })
        }
    }
}

@Composable
fun GoalCreator(context: Context, onFormValidChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    LaunchedEffect(Unit){
        DatabaseService.fetchTable(
            "category", context,
            { categories ->
                val newItems = JSONObject()
                for (i in 0 until categories.length()) {
                    val category = categories.getJSONObject(i)
                    val id = category.getString("id")
                    val name = category.getString("name")
                    newItems.put(id, name)
                }
                existingCategories.value = newItems
                println(existingCategories)
                Log.d("API_SUCCESS_CATEGORIES", categories.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }

    LaunchedEffect(goal.value, categoryValue.value) {
        val isValid = goal.value.isNotBlank() && categoryValue.value.isNotBlank()
        println("worked")
        onFormValidChanged(isValid)
    }

    val keys = existingCategories.value.keys().asSequence().toList()
    val verticalScroll = rememberScrollState()

    Column(
        modifier = modifier.padding(top = 48.dp, bottom = 48.dp)
    ) {
        OutlinedTextField(
            value = goal.value,
            label = { Text("Your goal") },
            placeholder = { Text("Eat more fruit...") },
            onValueChange = { newText ->
                goal.value = newText
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = getTextFieldColor(Color.White, C.LightBlue, Color.White),
            textStyle = Typography.bodyMedium
        )

        OutlinedTextField(
            value = categoryValue.value,
            onValueChange = { categoryValue.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    fieldSize = coordinates.size.toSize()
                },
            label = { Text("Select a category") },
            trailingIcon = {
                Icon(
                    Icons.Default.KeyboardArrowDown, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            colors = getTextFieldColor(Color.White, C.LightBlue, Color.White),
            textStyle = Typography.bodyMedium
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(with(LocalDensity.current) { fieldSize.width.toDp() })
                .crop(vertical = 8.dp)
                .verticalScroll(verticalScroll)
                .heightIn(max = 200.dp),
            containerColor = Color.Transparent,
            offset = DpOffset(0.dp, 4.dp),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = BorderStroke(
                width = 1.dp,
                color = C.LightBlue)
        ) {
            keys.forEachIndexed { index, key ->
                val value = existingCategories.value.getString(key)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = value,
                            color = (if (categoryValue.value == value) Color.White else Color.Black)
                        )
                    },
                    onClick = {
                        categoryValue.value = value
                        expanded = false
                    },
                    modifier = Modifier
                        .background(if (categoryValue.value == value) C.LightBlue else Color.White)
                )
            }
        }
    }
}

// TODO(): Create continue button, onClick save the new category if it is indeed new,
// TODO(): Continue to connect behaviors to this new goal


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
                ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, validGoal.value, {
                    val newGoalString = JSONObject("new goal").toString()
                    navController.navigate(route = CreateBehavior(newGoalString))
                })
            }
        }
    }
}