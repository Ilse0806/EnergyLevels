package com.example.microhabits.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.microhabits.helpers.crop
import com.example.microhabits.ui.theme.Color as C


@Composable
fun SingleDropdown(
    items: List<Any>,
    expanded: Boolean,
    selected: String,
    size: Size,
    onItemSelected: (String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    textAlign: (TextAlign) = TextAlign.Start
) {
    val verticalScroll = rememberScrollState()

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss(false) },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(with(LocalDensity.current) { size.width.toDp() })
            .crop(vertical = 8.dp)
            .verticalScroll(verticalScroll)
            .heightIn(max = 120.dp),
        containerColor = Color.Transparent,
        offset = DpOffset(0.dp, 4.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(
            width = 1.dp,
            color = C.LightBlue
        )
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = item.toString(),
                        color = (if (selected == item.toString()) Color.White else Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = textAlign
                    )
                },
                onClick = {
                    onItemSelected(item.toString())
                    onDismiss(false)
                },
                modifier = Modifier
                    .background(if (selected == item.toString()) C.LightBlue else Color.White)
            )
        }
    }
}

@Composable
fun MultipleDropdown(
    items: List<Any>,
    expanded: Boolean,
    selected: String,
    size: Size,
    onItemSelected: (String) -> Unit,
    onDismiss: (Boolean) -> Unit
) {

}