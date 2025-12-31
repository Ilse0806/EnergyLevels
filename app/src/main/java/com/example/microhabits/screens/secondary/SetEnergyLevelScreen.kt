package com.example.microhabits.screens.secondary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.services.EnergyService.getEnergyLevel
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetEnergyLevelScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (VariableModel.todayEnergy == null) {
            getEnergyLevel(LocalDate.now(), context)
        }
    }

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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetLevel() {
    var sliderValue by remember { mutableFloatStateOf(5f) }

    val batteryColor = when {
        sliderValue <= 2f -> Color.Red
        sliderValue <= 5f -> Color.Yellow
        else -> Color.Green
    }

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "What is your energy level today?",
            style = Typography.titleMedium
        )
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
            },
            valueRange = 1f .. 10f,
            modifier = Modifier
                .height(300.dp)
                .rotate(90f),
            colors = SliderColors(
                thumbColor = batteryColor,
                activeTrackColor = batteryColor,
                activeTickColor = Color.White,
                inactiveTrackColor = batteryColor,
                inactiveTickColor = Color.White,
                disabledThumbColor = Color.Gray,
                disabledActiveTrackColor = Color.Gray,
                disabledActiveTickColor = Color.Gray,
                disabledInactiveTrackColor = Color.Gray,
                disabledInactiveTickColor = Color.Gray
            )
        )
    }
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
                SetLevel()
            }
        }
    }
}
