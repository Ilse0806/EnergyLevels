package com.example.microhabits.screens.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.ExerciseDetails
import com.example.microhabits.components.CollapseContent
import com.example.microhabits.components.buttons.ButtonSecondary
import com.example.microhabits.components.getters.GetDifficulty
import com.example.microhabits.components.getters.GetTime
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.splitTime
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseDetailsScreen(navController: NavController, exerciseId: ExerciseDetails) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val exerciseItem = VariableModel.allExercises.find { it.id == exerciseId.id }

    exerciseItem?.let {
        Scaffold(
            topBar = {
                Header(
                    title = exerciseItem.name,
                    titleStyle = Typography.titleMedium.copy(color = Color.White),
                    icon = exerciseItem.iconFromString(),
                    context = context,
                    extraContent = {
                        Spacer(Modifier.padding(bottom = 12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val time = splitTime(exerciseItem.time)
                            GetTime(
                                minutes = time["minutes"] ?: 0,
                                textStyle = Typography.bodyLarge.copy(
                                    color = Color.White
                                ),
                                iconTint = C.Indigo,
                                hours = time["hours"] ?: 0
                            )
                            GetDifficulty(
                                difficulty = exerciseItem.difficulty,
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
//                TODO(): Maybe place this in a box as well, do display it more clearly ..?
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = exerciseItem.description,
                    style = Typography.bodyLarge
                )
                Spacer(Modifier.padding(12.dp))

                for (exercise in exerciseItem.exercises) {
                    var expanded by remember { mutableStateOf(false) }
                    val rotation by animateFloatAsState(
                        targetValue = if (expanded) 180f else 0f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    Column (
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(C.Indigo)
                            .padding(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exercise.name,
                                style = Typography.bodyLarge.copy(
                                    color = Color.White
                                ),
                            )
                            ButtonSecondary(
                                onClickAction = {
                                    expanded = !expanded
                                },
                                buttonColor = ButtonC.IndigoPrimary,
                                color = Color.White,
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Collapse",
                                        tint = Color.White,
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
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
//                                    TODO(): Add null check + add other content
                                    Text(
                                        text = exercise.description,
                                        style = Typography.bodyLarge.copy(
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ExerciseDetailsPreview() {
//    var expanded by remember { mutableStateOf(false) }
//    val rotation by animateFloatAsState(
//        targetValue = if (expanded) 180f else 0f,
//        animationSpec = tween(durationMillis = 300)
//    )
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            topBar = {
                Header(
                    title = "Go for a walk",
                    titleStyle = Typography.titleMedium.copy(color = Color.White),
//                    image = "https://www.transparentlabs.com/cdn/shop/articles/image1_1_985e1ff8-6709-4f15-a39f-28a5c282e780_1200x1200.jpg?v=1748023325",
                    context = LocalContext.current,
                    extraContent = {
                        Spacer(Modifier.padding(bottom = 12.dp))
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
                Spacer(Modifier.padding(12.dp))
                Text(
                    text = "description text ayyyay",
                    style = Typography.bodyLarge
                )
                Column (
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(C.Indigo)
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "fullBehavior.behavior.name",
                            style = Typography.bodyLarge.copy(
                                color = Color.White
                            ),
                        )
                        ButtonSecondary(
                            onClickAction = {
//                            expanded = !expanded
                            },
                            buttonColor = ButtonC.IndigoPrimary,
                            color = Color.White,
                            content = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Collapse",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
//                                    .rotate(rotation)
                                )
                            },
                            contentPadding = PaddingValues(0.dp)
                        )
                    }
                    CollapseContent(
                        expanded = true,
                        content = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "test",
                                    style = Typography.bodyLarge.copy(
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}