package com.example.microhabits.components.recommended

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.microhabits.components.DetailsBlock
import com.example.microhabits.models.superclasses.UpperActivity
import com.example.microhabits.ui.theme.Color as C
import com.example.microhabits.ui.theme.Typography

@Composable
fun DisplayRecommendedItems(
    title: String,
    list: SnapshotStateList<out UpperActivity>,
    context: Context,
    iconProvider: (UpperActivity) -> ImageVector?,
    imageProvider: (UpperActivity) -> String?,
    onClick: (UpperActivity) -> Unit,
    modifier: Modifier = Modifier,
    mainColor: Color = C.CoralRed,
    accentColor: Color = C.Red,
) {
    Text(
        text = title,
        style = Typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    for (item in list) {
        val icon = iconProvider(item)
        val image = imageProvider(item)
        DetailsBlock(
            title = item.name,
            icon = icon,
            image = image,
            minutes = item.time,
            difficulty = item.difficulty,
            attributes = item.attributes,
            modifier = modifier.clickable {
                onClick(item)
            },
            context = context,
            mainColor = mainColor,
            accentColor = accentColor
        )
        Spacer(Modifier.padding(4.dp))
    }
}