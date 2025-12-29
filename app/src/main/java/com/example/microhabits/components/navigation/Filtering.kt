package com.example.microhabits.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.microhabits.models.classes.FilterItem
import com.example.microhabits.ui.theme.Color as C
import com.example.microhabits.ui.theme.Typography

@Composable
fun Filtering(options: List<FilterItem<*>>) {
    LazyRow (
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        items(options) { option ->
            Box (
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (option.selected) C.Red else Color.Transparent)
                    .border(
                        1.dp,
                        C.Red,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
                    .clickable {
                        if (option.singleSelect) {
                            for (o in options) {
                                o.selected = false
                            }
                            option.selected = true
                        } else {
                            for (o in options) {
                                if (o.singleSelect) {
                                    o.selected = false
                                }
                            }
                            option.selected = !option.selected
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.name,
                    style = Typography.bodyMedium.copy(
                        fontWeight = if (option.selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (option.selected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Spacer(Modifier.padding(2.dp))
        }
    }
}
