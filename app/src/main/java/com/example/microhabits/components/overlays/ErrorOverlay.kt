package com.example.microhabits.components.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.Home
import com.example.microhabits.components.buttons.ButtonPrimary
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@Composable
fun ErrorOverlay(
    onReturn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale = remember { Animatable(0.0001f) }
    var returnVisibility by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    var displayVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit, displayVisible) {
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = if (displayVisible) 1f else 0f,
            animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing)
        )
        returnVisibility = !returnVisibility
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(C.Red)
            .clickable {
                displayVisible = false
                onReturn()
            },
    ) {
        IconButton(
            onClick = {
                displayVisible = false
                onReturn()
            },
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(C.Indigo)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Return",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.75f)
                    .aspectRatio(1f),
                tint = Color.White
            )
        }
        Icon(
            imageVector = Icons.Filled.ErrorOutline,
            contentDescription = "Error",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                )
                .align(Alignment.Center)
                .fillMaxWidth(0.75f)
                .aspectRatio(1f),
            tint = Color.White
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 150.dp),
        ) {
            Column (
                modifier = Modifier.height(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = returnVisibility,
                    enter = slideInVertically {
                        with(density) { 40.dp.roundToPx() }
                    } + fadeIn(
                        initialAlpha = 0.2f
                    ),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Text(
                        text = "ERROR: Something went wrong!" +
                                "Please try again later",
                        style = Typography.titleSmall.copy(color = Color.White),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.padding(12.dp))
                    ButtonPrimary(
                        buttonColor = ButtonC.IndigoPrimary,
                        color = Color.White,
                        onClickAction = {
                            displayVisible = false
                            onReturn()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to home screen"
                            )
                            Spacer(Modifier.padding(4.dp))
                            Text(
                                text = "Return to home",
                                style = Typography.bodyMedium
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowErrorPreview() {
    val navController = rememberNavController()
    ErrorOverlay(
        onReturn = { navController.navigate(route = Home) },
        modifier = Modifier.animateContentSize(),
    )
}