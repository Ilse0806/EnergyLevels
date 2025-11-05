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
import com.example.microhabits.Navigation
import com.example.microhabits.components.AddedBox
import com.example.microhabits.components.ButtonPrimary
import com.example.microhabits.components.Checkbox
import com.example.microhabits.components.ContinueButton
import com.example.microhabits.components.ReturnButton
import com.example.microhabits.models.CreateBehaviorModel.exampleBehaviors
import com.example.microhabits.models.CreateBehaviorModel.loadBehaviorsForCategory
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateBehaviorScreen (navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    if (VariableModel.selectedBehaviors.value.length() == 0){
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
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.selectedBehaviors.value.length() >= 2,
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
        if (exampleBehaviors.value.length() > 0) {
            Text(
                text = "Example behaviors",
                style = Typography.labelSmall,
            )
            exampleBehaviors.value.keys().forEach { key ->
                val cat = exampleBehaviors.value.getJSONObject(key)
                var isAdded by remember { mutableStateOf(cat.getBoolean("isAdded")) }

                fun onAdd(newAdd: Boolean) {
                    isAdded = newAdd
                    cat.put("isAdded", isAdded)
                    if (isAdded) {
                        val add = JSONObject(VariableModel.selectedBehaviors.value.toString()).apply {
                            put(key, cat)
                        }
                        VariableModel.selectedBehaviors.value = add
                    } else {
                        val remove = JSONObject(VariableModel.selectedBehaviors.value.toString())
                        remove.remove(key)
                        VariableModel.selectedBehaviors.value = remove
                    }
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Checkbox(Color.White, C.GoldenAmber, isAdded, ::onAdd, cat.getString("name"), isCheckbox = false)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizedBehaviors(modifier: Modifier = Modifier) {
    val existing = VariableModel.personalizedBehaviors.value.length()
    var amountOfInputs by remember { mutableIntStateOf(
        if (existing > 0) {
            0
        }
        else {
            1
        }
    ) }

    val allExistingItems = remember {
        val list = mutableListOf<String>()
        val json = VariableModel.personalizedBehaviors.value
        if (json.length() > 0) {
            json.keys().forEach { key ->
                val obj = json.getJSONObject(key)
                list.add(obj.getString("name"))
            }
        }
        list
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

            fun onAdd(newAdd: Boolean) {
                if (text.isNotBlank()) {
                    isAdded = newAdd
                    if (isAdded) {
                        val jsonObject = JSONObject().apply {
                            put("name", text)
                            put("isAdded", true)
                            put("impactSliderValue", 1f)
                            put("feasibilitySliderValue", 1f)
                        }
                        val add = JSONObject(VariableModel.selectedBehaviors.value.toString()).apply {
                            put("0$i", jsonObject)
                        }
                        VariableModel.selectedBehaviors.value = add
                        val addPersonal = JSONObject(VariableModel.personalizedBehaviors.value.toString()).apply {
                            put("0$i", jsonObject)
                        }
                        VariableModel.personalizedBehaviors.value = addPersonal
                        println(VariableModel.personalizedBehaviors.value)
                    } else {
                        val remove = JSONObject(VariableModel.selectedBehaviors.value.toString())
                        remove.remove("0$i")
                        VariableModel.selectedBehaviors.value = remove
                        val removePersonal = JSONObject(VariableModel.personalizedBehaviors.value.toString())
                        val inner = removePersonal.getJSONObject("0$i").put("isAdded", false)
                        VariableModel.personalizedBehaviors.value = inner
                        println(VariableModel.personalizedBehaviors.value)
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
        VariableModel.selectedBehaviors.value.keys().asSequence().forEach { id ->
            val behaviorObject = VariableModel.selectedBehaviors.value.getJSONObject(id)
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color.White)
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = behaviorObject.getString("name"),
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
                            val remove =
                                JSONObject(VariableModel.selectedBehaviors.value.toString())
                            remove.remove(id)
                            VariableModel.selectedBehaviors.value = remove
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
            ContinueButton(ButtonC.CoralRedPrimary, C.CoralRed, VariableModel.selectedBehaviors.value.length() > 5,
                {
//                    saveCategory(context)
                    navController.navigate(route = FocusMap)
                }
            )
            Box(Modifier.padding(innerPadding))
        }
    }
}