package com.example.microhabits.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.Color as C

@Composable
fun Checkbox (
    borderColor: Color,
    backgroundColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isCheckbox: Boolean = true,
    extraContent: @Composable () -> Unit = { }
) {
    Surface(
        color = if (checked) backgroundColor else Color.White,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) }
            .border(
                width = 1.dp,
                color = if (checked) borderColor else backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (!isCheckbox) Arrangement.SpaceBetween else Arrangement.Start,
            modifier = Modifier.padding(12.dp)
        ) {
            if (isCheckbox) {
                RoundedCheckbox(checked, onCheckedChange, borderColor, backgroundColor)
            }
            Text(
                text = text,
                style = Typography.bodyMedium,
                color = if (checked) Color.White else backgroundColor,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            if (!isCheckbox) {
                AddedBox(checked, onCheckedChange, borderColor, backgroundColor)
            }
            extraContent()
        }
    }
}

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    borderColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(borderColor)
            .border(1.dp, if (checked) borderColor else backgroundColor, RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = backgroundColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AddedBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    borderColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    Row (
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isError) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error: wrong input",
                tint = C.Red,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
        }
        if (checked) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Remove",
                tint = if (isError) C.Red else borderColor,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Added",
                tint = backgroundColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewCheckbox() {
    Checkbox(Color.White, C.Indigo, false, {println("hi")}, "test behavior")
}