package com.example.microhabits.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateGoal
import com.example.microhabits.DisplayGoal
import com.example.microhabits.FocusMap
import com.example.microhabits.Home
import com.example.microhabits.Navigation
import com.example.microhabits.components.BehaviorDetails
import com.example.microhabits.components.Checkbox
import com.example.microhabits.components.CollapseContent
import com.example.microhabits.components.ContinueButton
import com.example.microhabits.components.ReturnButton
import com.example.microhabits.components.SuccessOverlay
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.MeasuredInResult
import com.example.microhabits.models.NotificationFrequency
import com.example.microhabits.models.UserBehaviorWithBehavior
import com.example.microhabits.services.NewBehaviorService.calculateGoldenBehavior
import com.example.microhabits.services.NewBehaviorService.saveBehaviorAndGoal
import com.example.microhabits.services.NewBehaviorService.sortItems
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalTime
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectBehaviorScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    var showSuccessOverlay by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit){
        calculateGoldenBehavior()
    }

    val goldenItems = sortItems(VariableModel.goldenBehaviors)
    val remainingItems = sortItems(VariableModel.selectedBehaviors) - goldenItems

    Scaffold(
        bottomBar = {
            if (!WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        if (showSuccessOverlay) {
            val goalId = saveBehaviorAndGoal(VariableModel.goal.value, VariableModel.chosenBehaviors.value, context)
            SuccessOverlay(
                onGoHome = { navController.navigate(route = Home) },
                onViewGoal = {
                    val goalString = JSONObject(mapOf("id" to goalId)).toString()
                    navController.navigate(route = DisplayGoal(goalString))
                },
                modifier = Modifier.fillMaxSize().zIndex(1f),
                onDismiss = { navController.navigate(route = Home) },
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                navController.navigate(route = CreateGoal)
            })
            Text(
                text = "Select & specify",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Text(
                text = "Select the behaviors you want to use to achieve your goal and make changes to make them fit your needs",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )
            AvailableBehaviors(goldenItems, remainingItems)
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.chosenBehaviors.value.isNotEmpty(),
                {
                    showSuccessOverlay = true
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun AvailableBehaviors(goldenBehaviors: List<UserBehaviorWithBehavior>, remainingBehaviors: List<UserBehaviorWithBehavior>, modifier: Modifier = Modifier) {
    if (goldenBehaviors.isNotEmpty()) {
        Column(
            modifier = modifier
        ) {
            Text(
                "These are your golden behaviors:",
                style = Typography.titleSmall
            )
            goldenBehaviors.forEach { behavior ->
                val index = VariableModel.goldenBehaviors.indexOfFirst { it === behavior }
                BehaviorData(
                    fullBehavior = behavior,
                    color = C.Indigo,
                    colorDetails = C.LightBlue,
                    descriptionChanged = { description ->
                        if (index != -1) {
                            val update = VariableModel.goldenBehaviors[index]
                            update.behavior.description = description
                            VariableModel.goldenBehaviors[index] = update
                        }
                        println(VariableModel.goldenBehaviors[index].behavior.description)
                    },
                    notificationChanged = { checked, interval, frequency, pattern, time ->
                        if (index != -1) {
                            val update = VariableModel.goldenBehaviors[index]
                            update.userBehavior.notification = checked
                            update.userBehavior.notificationDay = DayOfWeek.valueOf(pattern.uppercase())
                            update.userBehavior.notificationInterval = interval.toInt()
                            update.userBehavior.notificationFrequency = NotificationFrequency.fromInput(frequency)
                            update.userBehavior.notificationTimeOfDay = time
                            VariableModel.goldenBehaviors[index] = update
                        }
                    },
                    anchorActionChanged = { anchorAction ->
                        if (index != -1) {
//                            TODO(): Make sure anchor action is saved in the correct database + table connected to the correct user_behavior id
                            val update = VariableModel.goldenBehaviors[index]
                            update.userBehavior.anchorAction = anchorAction
                            VariableModel.goldenBehaviors[index] = update
                        }
                    },
                    frequencyChanged = { measuredIn, times ->
                        if (index != -1) {
                            val update = VariableModel.goldenBehaviors[index]
                            update.behavior.measuredIn = MeasuredInResult.fromInput(measuredIn.lowercase())
                            update.userBehavior.timeS = times.toLong()
                            VariableModel.goldenBehaviors[index] = update
                        }
                    },
                    checkBox = true,
                )
            }
        }
    }
    if (remainingBehaviors.isNotEmpty()) {
        Column(
            modifier = modifier
        ) {
            Text(
                "These are your remaining behaviors:",
                style = Typography.titleSmall
            )
            remainingBehaviors.forEach { behavior ->
                val index = VariableModel.goldenBehaviors.indexOfFirst { it === behavior }
                BehaviorData(
                    fullBehavior = behavior,
                    color = C.Indigo,
                    colorDetails = C.LightBlue,
                    descriptionChanged = { description ->
                        if (index != -1) {
                            val update = VariableModel.goldenBehaviors[index]
                            update.behavior.description = description
                            VariableModel.goldenBehaviors[index] = update
                        }
                    },
                    notificationChanged = { checked, interval, frequency, pattern, time ->
                        if (index != -1) {
                            val update = VariableModel.goldenBehaviors[index]
                            update.userBehavior.notification = checked
                            update.userBehavior.notificationDay = DayOfWeek.valueOf(pattern.uppercase())
                            update.userBehavior.notificationInterval = interval.toInt()
                            update.userBehavior.notificationFrequency = NotificationFrequency.fromInput(frequency)
                            update.userBehavior.notificationTimeOfDay = time
                            VariableModel.goldenBehaviors[index] = update
                        }
                    },
                    anchorActionChanged = { anchorAction ->
                        if (index != -1) {
//                            TODO(): Make sure anchor action is saved in the correct database + table connected to the correct user_behavior id
                            val update = VariableModel.goldenBehaviors[index]
                            update.userBehavior.anchorAction = anchorAction
                            VariableModel.goldenBehaviors[index] = update
                        }
                    },
                    frequencyChanged = { measuredIn, times ->
                        if (index != -1) {
                            val update = VariableModel.goldenBehaviors[index]
                            update.behavior.measuredIn = MeasuredInResult.fromInput(measuredIn.lowercase())
                            update.userBehavior.timeS = times.toLong()
                            VariableModel.goldenBehaviors[index] = update
                        }
                    },
                    checkBox = true,
                )
            }
        }
    }
}

@Composable
fun BehaviorData(
    fullBehavior: UserBehaviorWithBehavior,
    color: Color,
    colorDetails: Color,
    descriptionChanged: (String) -> Unit,
    notificationChanged: (checked: Boolean, interval: String, frequency: String, pattern: String, time: LocalTime) -> Unit,
    anchorActionChanged: (String) -> Unit,
    frequencyChanged: (String, Int) -> Unit,
    checkBox: Boolean = false
) {
    var expanded by remember { mutableStateOf(true)}
    var isChecked by remember { mutableStateOf(VariableModel.chosenBehaviors.value.contains(fullBehavior)) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    1.dp,
                    if (checkBox) {
                        if (!isChecked) color
                        else Color.White
                    } else Color.White,
                    RoundedCornerShape(8.dp)
                )
                .background(
                    if (checkBox) {
                        if (isChecked) color
                        else Color.White
                    } else color
                )
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (checkBox) {
                fun onCheckBehavior(newChecked: Boolean) {
                    isChecked = newChecked
                    if (isChecked) {
                        val new = VariableModel.chosenBehaviors.value + fullBehavior
                        VariableModel.chosenBehaviors.value = new
                    } else {
                        val old = VariableModel.chosenBehaviors.value - fullBehavior
                        VariableModel.chosenBehaviors.value = old
                    }
                    println(VariableModel.chosenBehaviors.value)
                }

                Checkbox(
                    Color.White,
                    C.Indigo,
                    isChecked,
                    ::onCheckBehavior,
                    fullBehavior.behavior.name,
                    extraContent = {
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Collapse",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(36.dp)
                                .rotate(rotation)
                                .clickable { expanded = !expanded },
                            tint = if (checkBox) {
                                if (!isChecked) color
                                else Color.White
                            } else Color.White
                        )
                    }
                )
            } else {
                Text(
                    text = fullBehavior.behavior.name,
                    style = Typography.bodyMedium.copy(
                        color = Color.White
                    ),
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = 15.dp,
                        end = 12.dp,
                        bottom = 15.dp
                    )
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Collapse",
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(36.dp)
                        .rotate(rotation)
                        .clickable { expanded = !expanded },
                    tint = Color.White
                )
            }
        }
        CollapseContent(expanded, {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(
                        1.dp,
                        color,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                BehaviorDetails(
                    fullBehavior = fullBehavior,
                    descriptionChanged = descriptionChanged,
                    notificationChanged = notificationChanged,
                    anchorActionChanged = anchorActionChanged,
                    frequencyChanged = frequencyChanged,
                    modifier = Modifier.padding(12.dp),
                    color = colorDetails
                )
            }
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewSelectBehavior() {
    val navController = rememberNavController()
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
        ) {
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                navController.navigate(route = CreateGoal)
            })
            Text(
                text = "Select & specify",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Text(
                text = "Select the behaviors you want to use to achieve your goal and make changes to make them fit your needs",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )
//            AvailableBehaviors(goldenItems, remainingItems)
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                {
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}
