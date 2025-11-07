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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateGoal
import com.example.microhabits.FocusMap
import com.example.microhabits.Navigation
import com.example.microhabits.SelectBehavior
import com.example.microhabits.components.BehaviorDetails
import com.example.microhabits.components.Checkbox
import com.example.microhabits.components.CollapseContent
import com.example.microhabits.components.ContinueButton
import com.example.microhabits.components.ReturnButton
import com.example.microhabits.models.BehaviorModel.calculateGoldenBehavior
import com.example.microhabits.models.BehaviorModel.saveChangesBehavior
import com.example.microhabits.models.BehaviorModel.sortItems
import com.example.microhabits.models.UserBehaviorWithBehavior
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectBehaviorScreen(navController: NavController) {
    val scrollState = rememberScrollState()

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
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                navController.navigate(route = CreateGoal)
            })
            AvailableBehaviors(goldenItems, remainingItems)
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                {
                    saveChangesBehavior()
                    navController.navigate(route = SelectBehavior)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun AvailableBehaviors(goldenBehaviors: List<UserBehaviorWithBehavior>, remainingBehaviors: List<UserBehaviorWithBehavior>, modifier: Modifier = Modifier) {
    if (goldenBehaviors.isNotEmpty()) {
        println("Golden Behaviors: $goldenBehaviors")

        Column {
            Text(
                "These are your golden behaviors:",
                style = Typography.titleSmall
            )
            goldenBehaviors.forEach { behavior ->
                BehaviorData(behavior, C.Indigo, C.LightBlue, true)
            }
        }
    }
    if (remainingBehaviors.isNotEmpty()) {
        println("Remaining: $remainingBehaviors")
        Column {
            Text(
                "These are your remaining behaviors:",
                style = Typography.titleSmall
            )
            remainingBehaviors.forEach { behavior ->
                BehaviorData(behavior, C.Indigo, C.LightBlue, true)
            }
        }
    }
}

@Composable
fun BehaviorData(fullBehavior: UserBehaviorWithBehavior, color: Color, colorDetails: Color, checkBox: Boolean = false) {
    var expanded by remember { mutableStateOf(true)}
    var isChecked by remember { mutableStateOf(false) }

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
                    fullBehavior,
                    Modifier.padding(12.dp),
                    colorDetails
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
//            AvailableBehaviors(listOf(JSONObject()), listOf(JSONObject()))

            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                {
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}
