package com.example.microhabits.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.example.microhabits.ui.theme.Color as C
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getTextFieldColor
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun DatePicker(date: Long? = null, color: Color = C.CoralRed) {
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
        colors = getTextFieldColor(color),
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