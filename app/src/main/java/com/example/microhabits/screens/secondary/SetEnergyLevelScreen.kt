package com.example.microhabits.screens.secondary

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalSlider
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.Home
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.overlays.SuccessOverlay
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.EnergyLevel
import com.example.microhabits.models.enums.EnergyLevels
import com.example.microhabits.services.EnergyService.saveEnergyLevel
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getTextFieldColor
import java.time.LocalDate
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetEnergyLevelScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showSuccessOverlay by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (!showSuccessOverlay) {
                Header(
                    title = "Your energy level",
                    titleStyle = Typography.titleLarge.copy(color = Color.White),
                    context = LocalContext.current,
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
                onViewGoal = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                onDismiss = { navController.navigate(route = Home) },
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Spacer(Modifier.padding(12.dp))
            SetLevel(context, onClickAction = {
                showSuccessOverlay = true
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SetLevel(
    context: Context,
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit = {}
) {
    val sliderValue = rememberSliderState(VariableModel.todayEnergy.value?.percentage ?: 0f, valueRange = 0f..100f)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDragged by interactionSource.collectIsDraggedAsState()

    val batteryColor = when {
        sliderValue.value <= 50f -> {
            val fraction = sliderValue.value / 50f
            lerp(Color.Red, Color.Yellow, fraction)
        }
        else -> {
            val fraction = (sliderValue.value - 50f) / 50f
            lerp(Color.Yellow, Color.Green, fraction)
        }
    }

    val colors = SliderColors(
        thumbColor = if(isSystemInDarkTheme()) Color.White else C.Indigo,
        activeTrackColor = batteryColor,
        activeTickColor = Color.White,
        inactiveTrackColor = if(isSystemInDarkTheme()) Color(0xFF3A3741).copy(alpha = 0.6f) else Color(0xFFB0A8B8).copy(alpha = 0.6f),
        inactiveTickColor = Color.Black,
        disabledThumbColor = Color.Gray,
        disabledActiveTrackColor = Color.Gray,
        disabledActiveTickColor = Color.Gray,
        disabledInactiveTrackColor = Color.Gray,
        disabledInactiveTickColor = Color.Gray
    )

    var reason by remember { mutableStateOf(VariableModel.todayEnergy.value?.reason ?: "") }

    Column (
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What is your energy level like today?",
            style = Typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.padding(12.dp))

        VerticalSlider(
            state = sliderValue,
            modifier = Modifier.height(360.dp),
            interactionSource = interactionSource,
            reverseDirection = true,
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier
                        .width(96.dp),
                    colors = colors,
                    trackCornerSize = 24.dp,
                )
            },
            thumb = {
                Box (
                    contentAlignment = Alignment.Center
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        sliderState = sliderValue,
                        thumbSize = DpSize(96.dp, 16.dp),
                        colors = colors
                    )
                    if (isPressed or isDragged) {
                        Box(
                            modifier = Modifier
                                .offset(x = (-80).dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if(isSystemInDarkTheme()) Color(0xFF3A3741).copy(alpha = 0.6f) else Color(0xFFB0A8B8).copy(alpha = 0.6f)),
                        ) {
                            Text(
                                text = "${sliderValue.value.toInt()}%",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = Typography.bodyLarge
                            )
                        }
                    }
                }
            },
            colors = colors
        )
        Column(
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Text(
                text = "Reason for current energy level",
                style = Typography.bodyMedium.copy(
                    color = Color.Black
                ),
            )
            OutlinedTextField(
                value = reason,
                colors = getTextFieldColor(C.CoralRed),
                placeholder = { Text("Didn't eat a lot today")},
                onValueChange = { newReason ->
                    reason = newReason
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
            )
        }

        Spacer(Modifier.weight(1f))
        ContinueButton(
            colorB = ButtonC.CoralRedPrimary,
            color = Color.White,
            enabled = true,
            onClickAction = {
                VariableModel.todayEnergy.value = EnergyLevel(
                    date = LocalDate.now(),
                    level = EnergyLevels.fromValue(sliderValue.value),
                    percentage = sliderValue.value,
                    reason = reason
                )
                saveEnergyLevel(VariableModel.todayEnergy.value?.toMap()?: mapOf(
                    "date" to LocalDate.now(),
                    "energy_level" to EnergyLevels.fromValue(sliderValue.value).toString(),
                    "energy_level_percentage" to sliderValue.value,
                    "reason" to reason,
                ), context)

                onClickAction()
            },
            content = {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Save changes",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Save",
                        style = Typography.bodyLarge
                    )
                }
            }
        )    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun EnergyLevelPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            topBar = {
                Header(
                    title = "Your energy level",
                    titleStyle = Typography.titleLarge.copy(color = Color.White),
                    context = LocalContext.current,
                    headerBackground = C.CoralRed
                )
            },
            bottomBar = { Navigation(rememberNavController()) },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                SetLevel(LocalContext.current)
            }
        }
    }
}
