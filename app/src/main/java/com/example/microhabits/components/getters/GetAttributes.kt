package com.example.microhabits.components.getters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.microhabits.ui.theme.Typography

@Composable
fun GetAttributes(
    attributes: List<String>,
    modifier: Modifier = Modifier
) {
    val lastItem = attributes.last()
    Row (
        modifier = modifier.padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (attribute in attributes) {
            Text (
                text = attribute,
                style = Typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = Color.Black
                )
            )
            if (attribute != lastItem && attributes.size > 1) {
                Spacer(Modifier.padding(start = 8.dp))
                Box (
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black)
                        .width(1.dp)
                        .height(12.dp)
                )
                Spacer(Modifier.padding(start = 8.dp))
            }
        }
    }
}