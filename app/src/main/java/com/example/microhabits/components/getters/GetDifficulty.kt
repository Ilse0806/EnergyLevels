package com.example.microhabits.components.getters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.Color as C

@Composable
fun GetDifficulty(
    difficulty: Int,
    modifier: Modifier = Modifier,
    borderColor: Color = C.Indigo,
    textColor: Color = Color.Black
) {
    var bubbles by remember { mutableStateOf(
            mapOf(
                1 to "empty",
                2 to "empty",
                3 to "empty",
                4 to "empty",
                5 to "empty"
            )
        )
    }

    bubbles = bubbles.mapValues { (key, _) ->
        if (key <= difficulty) "filled" else "empty"
    }

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Difficulty:",
            style = Typography.bodyMedium.copy(
                color = textColor,
            ),
            modifier = Modifier.padding(end = 12.dp)
        )
        bubbles.forEach { bubble ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .background(if (bubble.value == "filled") borderColor else Color.White)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(100)
                    )
                    .size(12.dp)
            )
            Spacer(Modifier.padding(end = 2.dp))
        }
    }
}
