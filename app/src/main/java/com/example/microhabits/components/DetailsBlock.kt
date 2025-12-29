package com.example.microhabits.components

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.microhabits.components.getters.GetAttributes
import com.example.microhabits.components.getters.GetDifficulty
import com.example.microhabits.components.getters.GetTime
import com.example.microhabits.ui.theme.Color as C
import com.example.microhabits.ui.theme.Typography

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsBlock(
    title: String,
    minutes: Int,
    difficulty: Int,
    attributes: List<String>,
    context: Context,
    modifier: Modifier = Modifier,
    mainColor: Color = C.CoralRed,
    accentColor: Color = C.Red,
    image: String? = null,
    icon: ImageVector? = null,
    hours: Int = 0,
) {
    Row (
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            image?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(image)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(mainColor)
                        .size(88.dp)
                )
            }
            icon?.let {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(mainColor)
                        .size(88.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(63.dp),
                        tint = Color.White
                    )
                }
            }
            Column(
                modifier = Modifier
                    .height(88.dp)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = title,
                        style = Typography.bodyMedium.copy(
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    GetTime(
                        minutes = minutes,
                        hours = hours,
                        iconTint = accentColor,
                        textStyle = Typography.labelSmall.copy(
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    GetDifficulty(
                        difficulty = difficulty,
                        borderColor = accentColor,
                        textStyle = Typography.labelSmall.copy(
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                GetAttributes(
                    attributes = attributes
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "See more",
            tint = mainColor
        )

    }
}