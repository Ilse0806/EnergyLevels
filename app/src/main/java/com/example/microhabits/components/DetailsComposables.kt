package com.example.microhabits.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getSwitchColors
import com.example.microhabits.ui.theme.getTextFieldColor
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.microhabits.ui.theme.Color as C

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetails(goal: JSONObject, modifier: Modifier = Modifier, color: Color = C.LightBlue) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Description(goal.getString("description"), color)

        Text(
            text = "End date"
        )
        DatePicker()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BehaviorDetails(behavior: JSONObject, modifier: Modifier = Modifier, color: Color = C.LightBlue) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Description(behavior.getString("description"), color)

        NotificationSelector()
    }
}

@Composable
fun Description(description: String, color: Color = C.LightBlue) {
    var desc by remember { mutableStateOf(description) }

    Text(
        text = "Description",
        style = Typography.bodyMedium
    )
    OutlinedTextField(
        value = desc,
        label = { },
        onValueChange = { newText ->
            desc = newText
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = getTextFieldColor(color),
        textStyle = Typography.labelMedium
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationSelector(notificationOn: Boolean = true) {
    var checked by remember { mutableStateOf(notificationOn) }
    Row (
        modifier = Modifier.fillMaxWidth(),
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
            },
            colors = getSwitchColors(C.Indigo)
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

            ChoiceDropdown(listOf(1, 2, 3, 4, 5, 6), false, interval) { newInterval ->
                interval = newInterval
            }
            ChoiceDropdown(listOf("day", "week", "month"), false, frequency) { newFrequency ->
                frequency = newFrequency
            }

            Text(
                text = "at",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            ButtonPrimary(
                buttonColor = ButtonC.LightBluePrimary,
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
                ChoiceDropdown(listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"), true, pattern) { newPattern ->
                    pattern = newPattern
                }
            }
        }
        TimeInputComposable({showModal = false}, {newTime -> showModal = false; time = newTime}, showModal)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceDropdown(items: List<Any>, multiSelect: Boolean, startValue: String, onDismiss: (String) -> Unit) {
    var expanded by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf(startValue) }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
        ButtonPrimary(
            buttonColor = ButtonC.LightBluePrimary,
            color = Color.White,
            onClickAction = { expanded = !expanded },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    fieldSize = coordinates.size.toSize()
                }
                .padding(start = 4.dp, end = 4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            content = {
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
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    },
                    tint = Color.White
                )
            })
        if (multiSelect) {
            MultipleDropdown(
                items,
                expanded,
                selected,
                fieldSize,
                { newSelection -> selected = newSelection; onDismiss(newSelection) },
                { newVal -> expanded = newVal }
            )
        } else {
            SingleDropdown(
                items,
                expanded,
                selected,
                fieldSize,
                { newSelection -> selected = newSelection; onDismiss(newSelection) },
                { newVal -> expanded = newVal },
                TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputComposable(
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
    showModal: Boolean
){
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    if (showModal){
        AlertDialog(
            onDismissRequest = onDismiss,
            dismissButton = {
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Close modal"
                    )
                }
            },
            confirmButton = {
                IconButton(
                    onClick = {
                        val selectedTime = LocalTime.of(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        onConfirm(selectedTime)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save selection"
                    )
                }
            },
            text = {
                Column {
                    TimeInput(
                        state = timePickerState,
                        modifier = Modifier,
                        colors = TimePickerColors(
                            clockDialColor = Color.Transparent,
                            selectorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            periodSelectorBorderColor = Color.Transparent,
                            clockDialSelectedContentColor = Color.Transparent,
                            clockDialUnselectedContentColor = Color.Transparent,
                            periodSelectorSelectedContainerColor = C.Red,
                            periodSelectorUnselectedContainerColor = C.Indigo,
                            periodSelectorSelectedContentColor = C.Indigo,
                            periodSelectorUnselectedContentColor = C.Red,
//                    Actually used:
                            timeSelectorSelectedContainerColor = C.Indigo,
                            timeSelectorSelectedContentColor = C.LightTheme,
                            timeSelectorUnselectedContainerColor = C.LightBlue.copy(alpha = 0.5f),
                            timeSelectorUnselectedContentColor = C.Indigo.copy(alpha = 0.7f),
                        )
                    )
                }
            }
        )
    }
}


@Composable
fun DatePicker(date: Long? = null) {
    var selectedDate by remember { mutableStateOf(date) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "MM/DD/YYYY",
        onValueChange = { },
        label = {  },
        textStyle = Typography.labelMedium,
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        colors = getTextFieldColor(C.CoralRed),
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

