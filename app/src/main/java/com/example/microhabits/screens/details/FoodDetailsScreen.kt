package com.example.microhabits.screens.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.FoodDetails
import com.example.microhabits.components.getters.GetDifficulty
import com.example.microhabits.components.getters.GetTime
import com.example.microhabits.components.inputs.ButtonAndDropdown
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.overlays.ErrorOverlay
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.splitTime
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import java.util.Locale
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C


@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodDetailsScreen(navController: NavController, foodId: FoodDetails) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val foodItem = VariableModel.allFoods.find { it.id == foodId.id }

    foodItem?.let {
        Scaffold(
            topBar = {
                Header(
                    title = foodItem.name,
                    titleStyle = Typography.titleMedium.copy(color = Color.White),
                    image = foodItem.image,
                    context = context,
                    headerBackground = C.GoldenAmber,
                    extraContent = {
                        Spacer(Modifier.padding(bottom = 12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val time = splitTime(foodItem.time)
                            GetTime(
                                minutes = time["minutes"] ?: 0,
                                textStyle = Typography.bodyLarge.copy(
                                    color = Color.White
                                ),
                                iconTint = C.Indigo,
                                hours = time["hours"] ?: 0
                            )
                            GetDifficulty(
                                difficulty = foodItem.difficulty,
                                borderColor = C.Indigo,
                                textStyle = Typography.bodyLarge.copy(
                                    color = Color.White
                                ),
                            )
                        }
                    }
                )
            },
            bottomBar = {
                if (!WindowInsets.isImeVisible) {
                    Navigation(navController)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
                    .padding(innerPadding)
            ) {
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = foodItem.description,
                    style = Typography.bodyLarge
                )

                var portion by remember { mutableStateOf(4) }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ingredients: ",
                        style = Typography.titleSmall
                    )
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Portions:",
                            style = Typography.labelMedium
                        )
                        var expanded by remember { mutableStateOf(false) }
                        ButtonAndDropdown(
                            color = C.GoldenAmber,
                            list = listOf("1", "2", "4", "6", "8"),
                            buttonColors = ButtonC.GoldenAmberPrimary,
                            expandedParam = expanded,
                            enabled = true,
                            selectedParam = portion.toString(),
                            onClick = { newVal ->
                                portion = newVal.toInt()
                            },
                            modifier = Modifier.width(50.dp)
                        )
                    }
                }
//                TODO(): Add calculations function for the portions in combination with the ingredients
                for (item in foodItem.ingredients) {
                    Row (
                        modifier = Modifier.padding(bottom = 2.dp)
                    ) {
                        Text(
                            text = "•",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        val calculatedAmount = item.calculateAmount(portion, 4)
                        val formattedAmount =
                            if (calculatedAmount % 1.0 == 0.0)
                                calculatedAmount.toInt().toString()
                            else
                                String.format(Locale.US, "%.2f", calculatedAmount)
                        Text(
                            text = "${formattedAmount}${if (!item.amountExtra.isEmpty()) " " + item.amountExtra + " " else " "}${item.name}"
                        )
                    }
                }
                Text(
                    text = "Steps:",
                    style = Typography.titleSmall,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                foodItem.steps.forEachIndexed { index, item ->
                    val i = index + 1
                    Row (
                        modifier = Modifier.padding(bottom = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "$i.",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(
                            text = item,
                            style = Typography.bodyLarge,
                        )
                    }
                }
            }
        }
    } ?: run {
        ErrorOverlay()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun FoodDetailsPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            bottomBar = { Navigation(rememberNavController()) },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                var expanded by remember { mutableStateOf(true) }
                var selected by remember { mutableStateOf("4") }
                ButtonAndDropdown(
                    color = C.GoldenAmber,
                    list = listOf("1", "2", "4", "6", "8"),
                    buttonColors = ButtonC.GoldenAmberPrimary,
                    expandedParam = expanded,
                    enabled = true,
                    selectedParam = selected,
                    onClick = { newVal ->
                        selected = newVal
                    },
                )
            }
        }
    }
}