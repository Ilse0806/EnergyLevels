package com.example.microhabits.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microhabits.CreateGoal
import com.example.microhabits.FocusMap
import com.example.microhabits.Navigation
import com.example.microhabits.components.ContinueButton
import com.example.microhabits.components.ReturnButton
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FocusMapScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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

            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.selectedBehaviors.value.length() > 5,
                {
//                    saveCategory(context)
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}