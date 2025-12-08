package com.example.microhabits.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.ExerciseDetails
import com.example.microhabits.components.getters.GetDifficulty
import com.example.microhabits.components.getters.GetTime
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseDetailsScreen(navController: NavController, exerciseId: ExerciseDetails) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Header(
                title = "Go for a walk",
                titleStyle = Typography.titleMedium.copy(color = Color.White),
                image = "https://www.transparentlabs.com/cdn/shop/articles/image1_1_985e1ff8-6709-4f15-a39f-28a5c282e780_1200x1200.jpg?v=1748023325",
                context = context,
                extraContent = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GetTime(
                            minutes = 30,
                            textStyle = Typography.bodyLarge.copy(
                                color = Color.White
                            ),
                            iconTint = C.Indigo,
                            hours = 1
                        )
                        GetDifficulty(
                            difficulty = 3,
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
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            Text("hi")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ExerciseDetailsPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            topBar = {
                Header(
                    title = "Go for a walk",
                    titleStyle = Typography.titleMedium.copy(color = Color.White),
//                    image = "https://www.transparentlabs.com/cdn/shop/articles/image1_1_985e1ff8-6709-4f15-a39f-28a5c282e780_1200x1200.jpg?v=1748023325",
                    context = LocalContext.current,
                    extraContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            GetTime(
                                minutes = 30,
                                textStyle = Typography.bodyLarge.copy(
                                    color = Color.White
                                ),
                                iconTint = C.Indigo,
                                hours = 1
                            )
                            GetDifficulty(
                                difficulty = 3,
                                borderColor = C.Indigo,
                                textStyle = Typography.bodyLarge.copy(
                                    color = Color.White
                                ),
                            )
//                            Text(
//                                text = "Time: 45 min",
//                                style = Typography.bodyMedium.copy(
//                                    color = Color.White
//                                )
//                            )
//                            Text(
//                                text = "Difficulty: 45 min",
//                                style = Typography.bodyMedium.copy(
//                                    color = Color.White
//                                )
//                            )
                        }
                    }
                )
            },
            bottomBar = { Navigation(rememberNavController()) },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(innerPadding)
            ) {
                Text("test")
            }
        }
    }
}