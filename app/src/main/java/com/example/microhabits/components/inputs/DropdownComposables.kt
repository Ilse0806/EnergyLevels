package com.example.microhabits.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.microhabits.components.buttons.ButtonPrimary
import com.example.microhabits.helpers.crop
import com.example.microhabits.ui.theme.Typography


@Composable
fun SingleDropdown(
    color: Color,
    items: List<Any>,
    expanded: Boolean,
    selected: String,
    onItemSelected: (String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: (TextAlign) = TextAlign.Start,
) {
    val verticalScroll = rememberScrollState()

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss(false) },
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .crop(vertical = 8.dp)
            .verticalScroll(verticalScroll)
            .heightIn(max = 120.dp),
        containerColor = Color.Transparent,
        offset = DpOffset(0.dp, 4.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(
            width = 1.dp,
            color = color
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
                    .background(if (selected == item.toString()) color else Color.White)
            )
        }
    }
}

@Composable
fun MultipleDropdown(
    color: Color,
    items: List<Any>,
    expanded: Boolean,
    selectedItems: List<String>,
    size: Size,
    onSelectionChanged: (List<String>) -> Unit,
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
            color = color
        )
    ) {
        items.forEach { item ->
            val text = item.toString()
            DropdownMenuItem(
                text = {
                    Text(
                        text = text,
                        color = (if (text in selectedItems) Color.White else Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = textAlign
                    )
                },
                onClick = {
                    val newSelection = if (text in selectedItems) {
                        selectedItems - text
                    } else {
                        selectedItems + text
                    }
                    onSelectionChanged(newSelection)
                },
                modifier = Modifier
                    .background(if (text in selectedItems) color else Color.White)
            )
        }
    }
}

@Composable
fun ButtonAndDropdown(
    color: Color,
    buttonColors: ButtonColors,
    expandedParam: Boolean,
    enabled: Boolean,
    selectedParam: String,
    list: List<Any>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(expandedParam) }
    var selected by remember { mutableStateOf(selectedParam) }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    Box (
        modifier = modifier
    ){
        ButtonPrimary(
            buttonColor = buttonColors,
            color = Color.White,
            onClickAction = { if (enabled) expanded = !expanded },
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .padding(start = 4.dp, end = 4.dp)
                .onGloballyPositioned { coordinates ->
                    fieldSize = coordinates.size.toSize()
                },
            contentPadding = PaddingValues(horizontal = 8.dp),
            enabled = enabled,
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selected,
                        style = Typography.labelMedium.copy(
                            color = Color.White
                        ),
                    )
                    Spacer(Modifier.padding(start = 8.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        modifier = if (enabled) {
                            Modifier.clickable {
                                expanded = !expanded
                            }
                        } else {
                            Modifier
                        },
                        tint = Color.White
                    )
                }
            }
        )
        SingleDropdown(
            color,
            list,
            expanded,
            selected,
            { newSelection ->
                selected = newSelection
                onClick(newSelection)
            },
            { newVal -> expanded = newVal },
            modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() }),
        )
    }
}