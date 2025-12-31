package com.example.microhabits.components.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.microhabits.components.DatePicker
import com.example.microhabits.components.Description
import com.example.microhabits.models.classes.Goal
import com.example.microhabits.ui.theme.Color as C
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getTextFieldColor
import java.time.LocalDateTime
import kotlin.text.isNullOrBlank
import kotlin.text.isNullOrEmpty
import kotlin.text.orEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalEditor(
    goal: Goal?,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDeadlineChange: (LocalDateTime?) -> Unit,
    onFormValidChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    displayAll: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    var viewCategory by remember { mutableStateOf(false) }
    var viewOthers by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(goal?.name.orEmpty())}

    LaunchedEffect(viewOthers, goal?.name, goal?.category) {
        if (viewOthers) {
            val isValid = !goal?.name.isNullOrEmpty() && !goal.category.isNullOrEmpty()
            onFormValidChanged(isValid)
        } else {
            onFormValidChanged(false)
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Your goal: ",
            style = Typography.bodyMedium
        )
        OutlinedTextField(
            value = name,
            placeholder = { Text("Eat more fruit") },
            onValueChange = { newText ->
                name = newText
                onNameChange(newText)
//                goal?.let {
//                    it.name = newText
//                } ?: run {
//                    goal = Goal(
//                        name = newText,
//                        id = null,
//                        description = null,
//                        deadline = null,
//                        category = null
//                    )
//                }
            },
            colors = getTextFieldColor(Color.White, C.LightBlue, Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!goal?.name.isNullOrBlank()) {
                        viewCategory = true
                    }
                }
            ),
        )

        if (viewCategory or displayAll) {
            Text(
                text = "Category: (either food or exercise)",
                style = Typography.bodyMedium
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
            ) {
                OutlinedTextField(
                    value = goal?.category.orEmpty(),
                    onValueChange = { onCategoryChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            fieldSize = coordinates.size.toSize()
                        },
                    placeholder = { Text("Select a category") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.KeyboardArrowDown, "contentDescription",
                            Modifier.clickable { expanded = !expanded }
                        )
                    },
                    colors = getTextFieldColor(Color.White, C.LightBlue, Color.White),
                    textStyle = Typography.bodyMedium,
                    readOnly = true
                )

                SingleDropdown(
                    C.LightBlue,
                    listOf("Food", "Exercise"),
                    expanded,
                    goal?.category.orEmpty(),
                    { newSelection -> goal?.category = newSelection },
                    { newVal ->
                        expanded = newVal
                        if (!goal?.category.isNullOrBlank()) {
                            viewOthers = true
                        }
                    },
                    modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() })
                )
            }
            if (viewOthers or displayAll) {
                Description(
                    goal?.description.orEmpty(),
                    onChange = { newDesc ->
                        onDescriptionChange(newDesc)
                    },
                    color = C.LightBlue
                )

                Text(
                    text = "Deadline (end date):",
                    style = Typography.bodyMedium.copy(
                        color = Color.Black
                    ),
                )
//                maybe use onDeadlineChange here:
                DatePicker(color = C.LightBlue)
            }
        }
    }
}