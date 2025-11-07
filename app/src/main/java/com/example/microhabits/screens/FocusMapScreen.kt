package com.example.microhabits.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateGoal
import com.example.microhabits.FocusMap
import com.example.microhabits.Navigation
import com.example.microhabits.SelectBehavior
import com.example.microhabits.components.ButtonSecondary
import com.example.microhabits.components.CollapseContent
import com.example.microhabits.components.ContinueButton
import com.example.microhabits.components.ReturnButton
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FocusMapScreen(navController: NavController) {
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
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                navController.navigate(route = CreateGoal)
            })
            Text(
                text = "Set up your focus map",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Set up a focus map by deciding how likely it is you'll execute your behavior and how much impact this behavior has on your goal.",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )
            ScaleBehaviors()
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                {
                    navController.navigate(route = SelectBehavior)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun ScaleBehaviors(modifier: Modifier = Modifier) {
    VariableModel.selectedBehaviors.forEachIndexed { index, fullBehavior ->
        var expanded by remember { mutableStateOf(index == 0)}
        var impactSliderValue by remember { mutableFloatStateOf(fullBehavior.userBehavior.impactSliderValue) }
        var feasibilitySliderValue by remember { mutableFloatStateOf(fullBehavior.userBehavior.feasibilitySliderValue) }

        val rotation by animateFloatAsState(
            targetValue = if (expanded) 180f else 0f,
            animationSpec = tween(durationMillis = 300)
        )
        Column (
            modifier = modifier.padding(bottom = 16.dp)
        ) {
            Column (
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(C.GoldenAmber)
                    .padding(4.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fullBehavior.behavior.name,
                        style = Typography.bodyLarge,
                    )
                    ButtonSecondary(
                        onClickAction = {
                            expanded = !expanded
                        },
                        buttonColor = ButtonC.IndigoSecondary,
                        color = C.Indigo,
                        content = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Collapse",
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(rotation)
                            )
                        },
                        contentPadding = PaddingValues(0.dp)
                    )
                }
                CollapseContent(
                    expanded = expanded,
                    content = {
                        Column {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Low impact",
                                        style = Typography.bodyLarge
                                    )
                                    Text(
                                        text = "High impact",
                                        style = Typography.bodyLarge
                                    )
                                }
                                SliderFocusMap(
                                    impactSliderValue,
                                    { newValue ->
                                        impactSliderValue = newValue
                                        fullBehavior.userBehavior.impactSliderValue = newValue
                                    },
                                    C.Red,
                                    C.Indigo,
                                    modifier.padding(top = 4.dp)
                                )
                                if (index == 0) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "This has a low impact on my goal",
                                            style = Typography.labelSmall,
                                            modifier = Modifier.width(84.dp)
                                        )
                                        Text(
                                            text = "This has a high impact on my goal",
                                            style = Typography.labelSmall,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.width(84.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.padding(4.dp))
                            Column (
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Not likely",
                                        style = Typography.bodyLarge
                                    )
                                    Text(
                                        text = "Highly likely",
                                        style = Typography.bodyLarge
                                    )
                                }
                                SliderFocusMap(
                                    feasibilitySliderValue,
                                    { newValue ->
                                        feasibilitySliderValue = newValue
                                        fullBehavior.userBehavior.feasibilitySliderValue = newValue
                                    },
                                    C.Indigo,
                                    C.Red,
                                    modifier.padding(top = 4.dp)
                                )
                                if (index == 0) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "It's not likely that I'll do this behavior",
                                            style = Typography.labelSmall,
                                            modifier = Modifier.width(84.dp)
                                        )
                                        Text(
                                            text = "It's highly likely that I'll do this behavior",
                                            style = Typography.labelSmall,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.width(84.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SliderFocusMap(
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
    trackerColor: Color,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    Slider(
        value = sliderValue,
        onValueChange = { onValueChange(it) },
        valueRange = 1f..10f,
        steps = 8,
        colors = SliderColors(
            thumbColor = trackerColor,
            activeTrackColor = activeColor,
            activeTickColor = Color.White,
            inactiveTrackColor = activeColor.copy(alpha = 0.5f),
            inactiveTickColor = Color.White,
            disabledThumbColor = Color.Gray,
            disabledActiveTrackColor = Color.Gray,
            disabledActiveTickColor = Color.Gray,
            disabledInactiveTrackColor = Color.Gray,
            disabledInactiveTickColor = Color.Gray
        ),
        modifier = modifier.fillMaxWidth()
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewFocusMap() {
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
                text = "Set up your focus map",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Set up a focus map by deciding how likely it is you'll execute your behavior and how much impact this behavior has on your goal.",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            ScaleBehaviors()
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                {
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}
