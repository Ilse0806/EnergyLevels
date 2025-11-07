package com.example.microhabits.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.microhabits.models.UserBehaviorWithBehavior
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getSwitchColors
import org.json.JSONObject
import java.time.LocalTime
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetails(goal: JSONObject, modifier: Modifier = Modifier, color: Color = C.LightBlue) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Description(goal.getString("description"), color = color)

        Text(
            text = "End date"
        )
        DatePicker()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BehaviorDetails(
    fullBehavior: UserBehaviorWithBehavior,
    modifier: Modifier = Modifier,
    color: Color = C.LightBlue,
    buttonColor: ButtonColors = ButtonC.LightBluePrimary
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Description(fullBehavior.behavior.description, Modifier.padding(vertical = 0.dp), color)
        NotificationSelector(fullBehavior, color, buttonColor)
        AnchorActionInput(color, Modifier.padding(vertical = 24.dp))
        FrequencyInput(color, buttonColor)
    }
}

@Composable
fun Description(description: String, modifier: Modifier = Modifier, color: Color = C.LightBlue) {
    var desc by remember { mutableStateOf(description) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Description",
            style = Typography.bodyMedium,
        )
        OutlinedInputField(
            value = desc,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 4.dp, bottom = 12.dp),
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationSelector(fullBehavior: UserBehaviorWithBehavior, color: Color, buttonColor: ButtonColors, notificationOn: Boolean = true) {
    println(fullBehavior)
    println(VariableModel.selectedBehaviors)
    var checked by remember { mutableStateOf(notificationOn) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Notifications:",
            style = Typography.bodyMedium
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                if (checked) {
                    val new = VariableModel.chosenBehaviors.value + fullBehavior
                    VariableModel.chosenBehaviors.value = new
                } else {
                    val old = VariableModel.chosenBehaviors.value - fullBehavior
                    VariableModel.chosenBehaviors.value = old
                }
            },
            colors = getSwitchColors(color)
        )
    }
    if (checked) {
        var showModal by remember { mutableStateOf(false) }
        var interval by remember { mutableStateOf("1") }
        var frequency by remember { mutableStateOf("day") }
        var pattern by remember { mutableStateOf("Monday") }
        var time by remember { mutableStateOf(LocalTime.of(0, 0)) }

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Every",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(end = 8.dp)
            )

            ChoiceDropdown(color, buttonColor,listOf(1, 2, 3, 4, 5, 6), false, interval) { newInterval ->
                interval = newInterval
            }
            ChoiceDropdown(color, buttonColor,listOf("day", "week", "month"), false, frequency) { newFrequency ->
                frequency = newFrequency
            }

            Text(
                text = "at",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            ButtonPrimary(
                buttonColor = buttonColor,
                color = Color.White,
                onClickAction = { showModal = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                content = {
                    Text(
                        text = time.toString(),
                        style = Typography.labelMedium.copy(
                            color = Color.White
                        ),
                    )
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (frequency != "day") {
                Text(
                    text = "on",
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp)
                )
                ChoiceDropdown(color, buttonColor,listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"), true, pattern) { newPattern ->
                    pattern = newPattern
                }
            }
        }
        TimeInputComposable({showModal = false}, {newTime -> showModal = false; time = newTime}, showModal)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceDropdown(color: Color, buttonColors: ButtonColors, items: List<Any>, multiSelect: Boolean, startValue: String, onDismiss: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(startValue) }
    var selectedItems by remember { mutableStateOf(listOf<String>()) }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
        ButtonPrimary(
            buttonColor = buttonColors,
            color = Color.White,
            onClickAction = { expanded = !expanded },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    fieldSize = coordinates.size.toSize()
                }
                .padding(start = 4.dp, end = 4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            content = {
                Text(
                    text = if (multiSelect) {
                        if (selectedItems.isEmpty()) {
                            startValue
                        } else {
                            selectedItems.joinToString(", ")
                        }
                    } else selected,
                    style = Typography.labelMedium.copy(
                        color = Color.White
                    ),
                )
                Spacer(Modifier.padding(start = 8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    },
                    tint = Color.White
                )
            })
        if (multiSelect) {
            MultipleDropdown(
                color,
                items,
                expanded,
                selectedItems,
                fieldSize,
                { selectedItems = it },
                { newVal -> expanded = newVal }
            )
        } else {
            SingleDropdown(
                color,
                items,
                expanded,
                selected,
                { newSelection -> selected = newSelection; onDismiss(newSelection) },
                { newVal -> expanded = newVal },
                modifier = Modifier.width(with(LocalDensity.current) {fieldSize.width.toDp()}),
                TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorActionInput(color: Color, modifier: Modifier = Modifier) {
    var anchorAction by remember { mutableStateOf("Test") }

    Column(
        modifier = modifier
    ){
        Text(
            text = "Anchor action",
            style = Typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Select or create an anchor action to connect your behavior to so you can do your habit after this anchor action.\n" +
                    "For example: doing it after going to the toilet ",
            style = Typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedInputField(
            value = anchorAction,
            color = color,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}


@Composable
fun FrequencyInput(color: Color, buttonColors: ButtonColors, modifier: Modifier = Modifier) {
    val measureList = listOf("Time", "Amount of times")
    var measureExpanded by remember { mutableStateOf(false) }
    var measureSelected by remember { mutableStateOf("Measured in") }

    var enabled by remember { mutableStateOf(false) }

    var amountList by remember { mutableStateOf((1..100).toList()) }
    var amountExpanded by remember { mutableStateOf(false) }
    var amountSelected by remember { mutableIntStateOf(1) }

    val timeList = listOf("seconds", "minutes", "hours")
    var timeExpanded by remember { mutableStateOf(false) }
    var timeSelected by remember { mutableStateOf("seconds") }

    if (measureSelected != "Measured in") {
        enabled = true
        if (measureSelected == "Amount of times") {
            amountList = (1..100).toList()
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Frequency",
            style = Typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "When you do this behavior, for how long or how many times should do it for. Try to keep this amount small!",
            style = Typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row {

            ButtonAndDropdown(
                color,
                buttonColors,
                measureExpanded,
                true,
                measureSelected,
                measureList,
                { newValue ->
                    measureSelected = newValue
                },
                Modifier.weight(1f)
            )

            ButtonAndDropdown(
                color,
                buttonColors,
                amountExpanded,
                enabled,
                amountSelected.toString(),
                amountList,
                { newValue ->
                    amountSelected = newValue.toInt()
                },
                Modifier.weight(if (measureSelected == "Time") 1f else 2f)
            )

            if (measureSelected == "Time") {
                amountList = (1..59).toList()

                if (timeSelected == "hours") {
                    amountList = (1..23).toList()
                }

                ButtonAndDropdown(
                    color,
                    buttonColors,
                    timeExpanded,
                    enabled,
                    timeSelected,
                    timeList,
                    { newValue ->
                        timeSelected = newValue
                    },
                    Modifier.weight(1f)
                )
            }
        }
    }
}

