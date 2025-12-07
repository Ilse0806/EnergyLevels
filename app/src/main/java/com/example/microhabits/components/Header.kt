package com.example.microhabits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.microhabits.ui.theme.Color as C

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    titleStyle: TextStyle,
    modifier : Modifier = Modifier,
    headerBackground: Color = C.Red,
    image: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = C.CoralRed,
    extraContent: @Composable () -> Unit = {},
) {
    var extraPadding = PaddingValues(0.dp)
    Box (
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
            .background(headerBackground)
            .heightIn(min = 80.dp, max = 250.dp)
    ) {
        icon?.let {
            extraPadding = PaddingValues(top = 120.dp)
            Icon(
                imageVector = icon,
                contentDescription = "Header icon",
                tint = iconTint,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(150.dp)
                    .graphicsLayer {
                        scaleX = 1.5f
                        scaleY = 1.5f
                    }
            )
        }
        image?.let {
//                val request = ImageRequest.Builder(LocalContext.current)
//                    .data("https://picsum.photos/200")
//                    .listener(
//                        onError = { _, result ->
//                            Log.e("Coil", "Error: ${result.throwable}")
//                        },
//                        onSuccess = { _, _ -> Log.d("Coil", "Loaded!") }
//                    )
//                    .build()
            extraPadding = PaddingValues(top = 120.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Column (
            modifier = modifier
                .padding(extraPadding)
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = titleStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            extraContent()
        }
    }
}