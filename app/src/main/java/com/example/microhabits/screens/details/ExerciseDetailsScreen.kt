package com.example.microhabits.screens.details

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.microhabits.ExerciseDetails
import com.example.microhabits.Home
import com.example.microhabits.Progress
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.components.getters.GetDifficulty
import com.example.microhabits.components.getters.GetTime
import com.example.microhabits.components.navigation.Header
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.components.overlays.CompletedActivityOverlay
import com.example.microhabits.components.overlays.SuccessOverlay
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.splitTime
import com.example.microhabits.models.classes.SingleExercise
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseDetailsScreen(navController: NavController, exerciseId: ExerciseDetails) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val exerciseItem = VariableModel.allExercises.find { it.id == exerciseId.id }

    var showCompletedOverlay by remember { mutableStateOf(false) }

    exerciseItem?.let {
        Scaffold(
            topBar = {
                if (!showCompletedOverlay) {
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
                }
            },
            bottomBar = {
                if (!WindowInsets.isImeVisible && !showCompletedOverlay) {
                    Navigation(navController)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
        ) { innerPadding ->
            if (showCompletedOverlay) {
                CompletedActivityOverlay(
                    onGoHome = { navController.navigate(route = Home) },
                    onViewProgress = {
                        navController.navigate(route = Progress)
                    },
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
                Text(
                    text = exerciseItem.description,
                    style = Typography.bodyLarge
                )
                Spacer(Modifier.padding(12.dp))

                for (exercise in exerciseItem.exercises) {
                    ShowSingleExercise(exercise, context)
                }
                Spacer(Modifier.weight(1f))
                ContinueButton(
                    colorB = ButtonC.GoldenAmberPrimary,
                    color = Color.White,
                    enabled = true,
                    onClickAction = {
//                        TODO(): Add save function to save data into progress table
                        showCompletedOverlay = true
                    },
                    content = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Completed exercise",
                                style = Typography.bodyLarge
                            )
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Save changes",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ShowSingleExercise(exercise: SingleExercise, context: Context) {
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
            println(exercise.video)
            println(exercise.image)
            if (exercise.video != "https://" && !exercise.video.isNullOrEmpty()) {
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        val mediaItem = MediaItem.fromUri(exercise.video!!)
                        setMediaItem(mediaItem)
                        prepare()
                    }
                }
                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true // Set to false for background/auto-play loops
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black)
                        .size(88.dp)
                )
            } else if (exercise.image != "https://" && !exercise.image.isNullOrEmpty()) {
                println("image works 2")
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(exercise.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .size(88.dp)
                )
            }

            Column {
                SingleExerciseContent(exercise)
            }
        }
    }
    Spacer(Modifier.padding(4.dp))
}

@Composable
fun SingleExerciseContent(exercise: SingleExercise) {
    var completed by remember { mutableStateOf(false) }

    val buttonColors = if (completed) {
        ButtonColors(
            containerColor = C.Green,
            contentColor = Color.White,
            disabledContainerColor = C.Green,
            disabledContentColor = Color.White
        )
    } else {
        ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = C.Green,
            disabledContentColor = Color.White
        )
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp,
                    topEnd = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = exercise.name,
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Button (
            colors = buttonColors,
            onClick = {
                completed = !completed
            },
            border = BorderStroke(1.dp, C.Green),
        ) {
            Text(
                text = "Completed",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Spacer(Modifier.padding(horizontal = 2.dp))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Complete",
                tint = if (completed) Color.White else C.Green
            )
        }
    }
    Text(
        text = exercise.description,
        style = Typography.labelSmall.copy(
            color = Color.White
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
    val time = splitTime(exercise.time)
    val text = time["hours"]?.let {
        if (it > 0 ) {
            "${time["hours"]} hr ${time["minutes"]} min"
        } else {
            "${time["minutes"]} min"
        }
    }
    Text(
        text = "Duration: $text",
        style = Typography.labelSmall.copy(
            color = Color.White
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
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
                ShowSingleExercise(SingleExercise(id = 1, name = "test", description = "desc", time = 45, image = "", video = ""), LocalContext.current)
                ShowSingleExercise(SingleExercise(id = 1, name = "test", description = "desc", time = 45, image = "", video = ""), LocalContext.current)
                Spacer(Modifier.weight(1f))
                ContinueButton(
                    colorB = ButtonC.CoralRedPrimary,
                    color = Color.White,
                    enabled = true,
                    onClickAction = {
                    },
                    content = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Completed exercise",
                                style = Typography.bodyLarge,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Complete exercise",
                            )
                        }
                    }
                )
            }
        }
    }
}