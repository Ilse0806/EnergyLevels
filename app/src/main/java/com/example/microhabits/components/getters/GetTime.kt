package com.example.microhabits.components.getters

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.microhabits.R
import com.example.microhabits.ui.theme.Typography

@Composable
fun GetTime(
    minutes: Int,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    iconTint: Color = Color.Black,
    hours: Int = 0,
) {
    val icon = when {
        minutes in 0..20 && hours <= 0 -> painterResource(id = R.drawable.clock_loader_20_24)
        minutes in 20..40 && hours <= 0 -> painterResource(id = R.drawable.clock_loader_40_24)
        (minutes in 40..60 && hours <= 0) or (minutes in 0..20 && hours <= 1) -> painterResource(id = R.drawable.clock_loader_60_24)
        minutes in 20..60 && hours <= 1 -> painterResource(id = R.drawable.clock_loader_80_24)
        hours <= 2 -> painterResource(id = R.drawable.clock_loader_90_24)
        else -> painterResource(id = R.drawable.clock_loader_40_24)
    }

    val text = if (hours > 0 ) {
        "$hours hr $minutes min"
    } else {
        "$minutes min"
    }

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Time: $text",
            style = Typography.bodyMedium.copy(
                color = textColor
            ),
            modifier = Modifier.padding(end = 12.dp)
        )
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconTint,
        )

    }
}