package com.example.microhabits.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateGoal
import com.example.microhabits.FocusMap
import com.example.microhabits.Navigation
import com.example.microhabits.SelectBehavior
import com.example.microhabits.components.BehaviorDetails
import com.example.microhabits.components.ButtonPrimary
import com.example.microhabits.components.CollapseContent
import com.example.microhabits.components.ContinueButton
import com.example.microhabits.components.MultipleDropdown
import com.example.microhabits.components.ReturnButton
import com.example.microhabits.components.SingleDropdown
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getSwitchColors
import org.json.JSONObject
import java.time.LocalTime
import java.util.Calendar
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectBehaviorScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit){
        calculateGoldenBehavior()
    }

    val goldenItems = sortItems(VariableModel.goldenBehaviors.value)
    val remainingItems = sortItems(VariableModel.selectedBehaviors.value)

    Scaffold(
        bottomBar = {
            if (!WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                navController.navigate(route = CreateGoal)
            })
            AvailableBehaviors(goldenItems, remainingItems)
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                {
                    navController.navigate(route = SelectBehavior)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}

fun calculateGoldenBehavior() {
    VariableModel.selectedBehaviors.value.keys().asSequence().withIndex().forEach { (index, behavior) ->
        val data = VariableModel.selectedBehaviors.value.get(behavior) as JSONObject
        val impactValue = data.getInt("impactSliderValue")
        val feasibilityValue = data.getInt("feasibilitySliderValue")

        if (impactValue > 5 && feasibilityValue > 5) {
            val newObject = JSONObject(VariableModel.goldenBehaviors.value.toString()).apply {
                put(behavior, data)
            }
            VariableModel.goldenBehaviors.value = newObject
        }
    }
}

fun sortItems(allItems: JSONObject): List<JSONObject> {
    val items = allItems.keys().asSequence()
        .map { key -> allItems.getJSONObject(key) }
        .toList()
    val sortedItems = items.sortedWith(
        compareBy<JSONObject>(
            { it.getInt("feasibilitySliderValue") },
            { it.getInt("impactSliderValue") }
        )
    )

    return sortedItems
}

@Composable
fun AvailableBehaviors(goldenBehaviors: List<JSONObject>, remainingBehaviors: List<JSONObject>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true)}

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    if (goldenBehaviors.isNotEmpty()) {
        Column {
            Text(
                "These are your golden behaviors:",
                style = Typography.titleSmall
            )
            goldenBehaviors.forEach { behavior ->
//                TODO(): create view for each behavior, maybe make it a function, since the remaining behaviors has the same design
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    1.dp,
                    Color.White,
                    RoundedCornerShape(8.dp)
                )
                .background(C.Indigo),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Behavior 1",
                style = Typography.bodyMedium.copy(
                    Color.White
                ),
                modifier = Modifier.padding(start = 12.dp, top = 15.dp, end = 12.dp, bottom = 15.dp)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Collapse",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(36.dp)
                    .rotate(rotation),
                tint = Color.White
            )
        }
        CollapseContent(expanded, {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(
                        1.dp,
                        C.Indigo,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                BehaviorDetails(
                    JSONObject("""{ "category_id" : 2, "description":"Do a push-up", "id":1, "measured_in":"amount of times", "name":"Do a push-up" }"""),
                    Modifier.padding(12.dp)
                )

            }
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewSelectBehavior() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            if (!WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        ) {
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                navController.navigate(route = CreateGoal)
            })
            AvailableBehaviors(listOf(JSONObject()), listOf(JSONObject()))

            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, true,
                {
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}
