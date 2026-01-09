package com.example.microhabits.screens.secondary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import com.example.microhabits.ExerciseDetails
import com.example.microhabits.Home
import com.example.microhabits.components.inputs.ActivityEditor
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.overlays.SuccessOverlay
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.services.CreatorService.saveNewExercise
import com.example.microhabits.ui.theme.Color as C
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateExerciseScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showSuccessOverlay by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (!showSuccessOverlay) {
                Header(
                    title = "New exercise",
                    titleStyle = Typography.titleLarge.copy(color = Color.White),
                    context = context,
                    headerBackground = C.LightBlue
                )
            }
        },
        bottomBar = {
            if (!WindowInsets.isImeVisible && !showSuccessOverlay) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        if (showSuccessOverlay) {
            var show by remember { mutableStateOf(false) }
            saveNewExercise(VariableModel.newExercise.value, context)
            LaunchedEffect(VariableModel.newExercise.value.id) {
                show = true
            }
            if (show) {
                SuccessOverlay(
                    onGoHome = { navController.navigate(route = Home) },
                    onViewGoal = {
                        navController.navigate(route = ExerciseDetails(VariableModel.newExercise.value.id))
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
            ActivityEditor("exercise", {
                showSuccessOverlay = true
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CreateExercisePreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            topBar = {
                Header(
                    title = "New exercise",
                    titleStyle = Typography.titleLarge.copy(color = Color.White),
                    context = LocalContext.current,
                    headerBackground = C.LightBlue
                )
            },
            bottomBar = { Navigation(rememberNavController()) },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                ActivityEditor("exercise", {})
            }
        }
    }
}