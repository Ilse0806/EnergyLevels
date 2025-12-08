package com.example.microhabits.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.microhabits.CreateGoal
import com.example.microhabits.FocusMap
import com.example.microhabits.Home
import com.example.microhabits.components.buttons.AddedBox
import com.example.microhabits.components.buttons.ButtonPrimary
import com.example.microhabits.components.buttons.Checkbox
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.components.buttons.ReturnButton
import com.example.microhabits.components.navigation.Navigation
import com.example.microhabits.models.classes.Behavior
import com.example.microhabits.services.NewBehaviorService.exampleBehaviors
import com.example.microhabits.services.NewBehaviorService.loadBehaviorsForCategory
import com.example.microhabits.models.classes.UserBehavior
import com.example.microhabits.models.classes.UserBehaviorWithBehavior
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.enums.NotificationFrequency
import com.example.microhabits.ui.theme.Typography
import java.time.LocalDate
import java.time.LocalTime
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateBehaviorScreen (navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    if (VariableModel.selectedBehaviors.isEmpty()){
        loadBehaviorsForCategory(VariableModel.goalCategory.value.get("id") as Int, context)
    }
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
            Text(
                text = "Connect your goal to a new behavior",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            ExistingBehaviors()
            PersonalizedBehaviors()
            SelectedBehaviors()
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.selectedBehaviors.size >= 2,
                {
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun ExistingBehaviors(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 48.dp)
    ) {
        if (exampleBehaviors.isNotEmpty()) {
            Text(
                text = "Example behaviors",
                style = Typography.labelSmall,
            )
            exampleBehaviors.forEachIndexed { index, key ->
                val cat = exampleBehaviors[index]
                var isAdded by remember { mutableStateOf(cat.userBehavior.isAdded) }

                fun onAdd(newAdd: Boolean) {
                    isAdded = newAdd
                    cat.userBehavior.isAdded = isAdded
                    if (isAdded) {
                        val newList = VariableModel.selectedBehaviors.apply {
                            add(cat)
                        }
                        VariableModel.selectedBehaviors = newList
                    } else {
                        val newList = VariableModel.selectedBehaviors.apply {
                            remove(cat)
                        }
                        VariableModel.selectedBehaviors = newList
                    }
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Checkbox(Color.White, C.GoldenAmber, isAdded, ::onAdd, cat.behavior.name, isCheckbox = false)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizedBehaviors(modifier: Modifier = Modifier) {
    val existing = VariableModel.personalizedBehaviors.size
    var amountOfInputs by remember { mutableIntStateOf(
        if (existing > 0) {
            0
        }
        else {
            1
        }
    ) }

    val allExistingItems = remember(VariableModel.personalizedBehaviors) {
        VariableModel.personalizedBehaviors.map { it.behavior.name }
    }
    var total by remember { mutableIntStateOf(amountOfInputs + existing) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 32.dp)
    ) {
        Text(
            text = "Create your own behaviors",
            style = Typography.labelSmall,
            modifier = Modifier.fillMaxWidth(),
        )

        for (i in 0 until total) {
            var text by remember(i) { mutableStateOf(allExistingItems.getOrNull(i)?: "") }
            var isAdded by remember(i) { mutableStateOf(allExistingItems.getOrNull(i) != null) }
            var isError by remember(i) { mutableStateOf(false) }
            var newBehavior by remember { mutableStateOf(UserBehaviorWithBehavior(
                i,
                UserBehavior(
                    id = null,
                    goldenBehavior = false,
                    oldBehavior = false,
                    progress = null,
                    timeS = 1,
                    notification = true,
                    completedToday = false,
                    notificationFrequency = NotificationFrequency.DAILY,
                    notificationInterval = null,
                    notificationDay = LocalDate.now().dayOfWeek,
                    notificationTimeOfDay = LocalTime.of(10, 0),
                    startDate = LocalDate.now(),
                    behaviorId = null,
                    userId = VariableModel.userId,
                    goalId = null,
                    isAdded = isAdded
                ),
                Behavior(
                    id = null,
                    name = text,
                    description = "",
                    categoryId = VariableModel.goalCategory.value.get("id") as Int,
                )
            )) }

            @RequiresApi(Build.VERSION_CODES.O)
            fun onAdd(newAdd: Boolean) {
                if (text.isNotBlank()) {
                    isAdded = newAdd
                    newBehavior.behavior.name = text
                    newBehavior.userBehavior.isAdded = isAdded
                    if (isAdded) {
                        val add = VariableModel.selectedBehaviors.apply {
                            add(newBehavior)
                        }
                        VariableModel.selectedBehaviors = add
                        val addPersonal = VariableModel.personalizedBehaviors.apply {
                            add(newBehavior)
                        }
                        VariableModel.personalizedBehaviors = addPersonal
                    } else {
                        VariableModel.selectedBehaviors.removeAll { it.id == newBehavior.id }
                        VariableModel.personalizedBehaviors.removeAll { it.id == newBehavior.id }
                    }
                } else {
                    isError = true
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))
            Surface(
                color = if (isAdded) C.GoldenAmber else Color.White,
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = if (isAdded) Color.White else if (isError) C.Red else C.GoldenAmber,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(12.dp)
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                            isError = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        singleLine = true,
                        textStyle = Typography.bodyMedium.copy(
                            color = if (isAdded) Color.White else C.GoldenAmber
                        ),
                        decorationBox = { innerTextField ->
                            innerTextField()
                        },
                    )
                    AddedBox(isAdded, ::onAdd, Color.White, C.GoldenAmber, isError = isError)
                }
            }
            if (isError) {
                Text(
                    text = "No behavior entered",
                    style = Typography.labelSmall.copy(
                        color = C.Red
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
        ButtonPrimary(
            ButtonC.IndigoPrimary,
            C.Indigo,
            {
                total ++
            },
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            content = {
                Text (
                    text = "Extra field",
                    style = Typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add an extra field",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
        )
    }
}

@Composable
fun SelectedBehaviors(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(C.CoralRed)
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
    ) {
        VariableModel.selectedBehaviors.forEach { fullBehavior ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color.White)
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fullBehavior.behavior.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    style = Typography.bodyMedium.copy(
                        color = Color.White
                    )
                )
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Remove selected behavior",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            VariableModel.selectedBehaviors.remove(fullBehavior)
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CreateBehaviorPreview() {
    val scrollState = rememberScrollState()
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
                .verticalScroll(scrollState)
        ) {
            ReturnButton(ButtonC.CoralRedPrimary, C.CoralRed,{
                navController.navigate(route = Home)
            })
            Text(
                text = "Connect your goal to a new behavior",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center
            )
            ExistingBehaviors()
            PersonalizedBehaviors()
            SelectedBehaviors()
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.selectedBehaviors.size > 5,
                {
//                    saveCategory(context)
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}