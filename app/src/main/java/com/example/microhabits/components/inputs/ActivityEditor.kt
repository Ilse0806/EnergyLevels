package com.example.microhabits.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.microhabits.components.CollapseContent
import com.example.microhabits.components.DefaultInput
import com.example.microhabits.components.Description
import com.example.microhabits.components.DifficultyInput
import com.example.microhabits.components.IconInput
import com.example.microhabits.components.TimeInput
import com.example.microhabits.components.buttons.ButtonPrimary
import com.example.microhabits.components.buttons.Checkbox
import com.example.microhabits.components.buttons.ContinueButton
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.toImageVector
import com.example.microhabits.models.classes.Ingredient
import com.example.microhabits.models.classes.SingleExercise
import com.example.microhabits.ui.theme.Typography
import com.example.microhabits.ui.theme.getTextFieldColor
import com.example.microhabits.ui.theme.ButtonColors as ButtonC
import com.example.microhabits.ui.theme.Color as C

@Composable
fun ActivityEditor(
    activity: String,
    saveAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var difficulty by remember { mutableIntStateOf(1) }
    val attributes = remember { mutableListOf<String>() }
    var icon by remember { mutableStateOf(Icons.AutoMirrored.Filled.DirectionsWalk) }

    val ingredients = remember { mutableStateListOf<Ingredient>() }
    val exercises = remember { mutableStateListOf<SingleExercise>() }
    val steps = remember { mutableStateListOf<String>() }

    Column (
        modifier = modifier
    ) {
        DefaultInput(
            name = name,
            onChange = { newName ->
                name = newName
                if (activity == "recipe") {
                    VariableModel.newRecipe.value.name = newName
                } else {
                    VariableModel.newExercise.value.name = newName
                }
            },
            color = C.LightBlue,
            placeholder = "New $activity",
            title = "Name"
        )
        Description(
            description = desc,
            onChange = { newDesc ->
                desc = newDesc
                if (activity == "recipe") {
                    VariableModel.newRecipe.value.description = newDesc
                } else {
                    VariableModel.newExercise.value.description = newDesc
                }
            },
            color = C.LightBlue
        )
        Row {
            Column (
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                TimeInput(
                    time = time,
                    onChange = { newTime ->
                        time = newTime
                        if (activity == "recipe") {
                            VariableModel.newRecipe.value.time = newTime.toIntOrNull() ?: 0
                        } else {
                            VariableModel.newExercise.value.time = newTime.toIntOrNull() ?: 0
                        }
                    },
                    color = C.LightBlue,
                )
            }
            Column (
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                DifficultyInput(
                    difficulty = difficulty,
                    onChange = { newDiff ->
                        difficulty = newDiff.toInt()
                        if (activity == "recipe") {
                            VariableModel.newRecipe.value.difficulty = newDiff.toInt()
                        } else {
                            VariableModel.newExercise.value.difficulty = newDiff.toInt()
                        }
                    }
                )
            }
        }
        if (activity == "recipe") {
            var typeExpanded by remember { mutableStateOf(false) }
            var type by remember { mutableStateOf("Dinner") }
            var fieldSizeType by remember { mutableStateOf(Size.Zero)}

            Text(
                text = "Type",
                style = Typography.bodyMedium
            )
            OutlinedTextField(
                value = type,
                placeholder = { Text("Dinner") },
                onValueChange = { newType ->
                    type = newType
                    if (activity == "recipe") {
                        VariableModel.newRecipe.value.type = newType
                    }
                },
                colors = getTextFieldColor(C.LightBlue, Color.White, Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp)
                    .onGloballyPositioned { coordinates ->
                        fieldSizeType = coordinates.size.toSize()
                    },
                trailingIcon = {
                    Icon(
                        imageVector = if (typeExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select type",
                        modifier = Modifier.clickable {
                            typeExpanded = !typeExpanded
                        }
                    )
                },
                enabled = false
            )
            SingleDropdown(
                color = C.LightBlue,
                items = listOf("Breakfast", "Lunch", "Dinner", "Snacks"),
                expanded = typeExpanded,
                selected = type,
                onItemSelected = {
                    type = it
                },
                onDismiss = {
                    typeExpanded = false
                },
                modifier = Modifier.width(with(LocalDensity.current) { fieldSizeType.width.toDp() }),
            )
            Connector(
                "recipe",
                ingredients,
                exercises,
                steps,
                onIngredientsChange = {
                    ingredients.clear()
                    ingredients.addAll(it)
                    println(ingredients)
                },
                onExercisesChange = {
                    exercises.clear()
                    exercises.addAll(it)
                    println(exercises)
                },
                onStepsChange = {
                    steps.clear()
                    steps.addAll(it)
                    VariableModel.newRecipe.value.steps = steps
                }
            )
        } else {
            var attributesExpanded by remember { mutableStateOf(false) }
            var fieldSizeAttributes by remember { mutableStateOf(Size.Zero)}

            Text(
                text = "Attributes (separate with , )",
                style = Typography.bodyMedium
            )
            OutlinedTextField(
                value = attributes.joinToString(", "),
                placeholder = { Text("Select attributes") },
                onValueChange = { newAttr ->
//                    attributes.add(newAttr)
//                    VariableModel.newExercise.value.attributes = attributes.toList()
                },
                colors = getTextFieldColor(C.LightBlue, Color.White, Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp)
                    .onGloballyPositioned { coordinates ->
                        fieldSizeAttributes = coordinates.size.toSize()
                    },
                enabled = false,
                trailingIcon = {
                    Icon(
                        imageVector = if (attributesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select attributes",
                        modifier = Modifier.clickable {
                            attributesExpanded = !attributesExpanded
                        }
                    )
                }
            )
            MultipleDropdown(
                color = C.LightBlue,
                selectedItems = attributes,
                items = VariableModel.existingAttributes,
                expanded = attributesExpanded,
                size = fieldSizeAttributes,
                onSelectionChanged = {
                    attributes.clear()
                    attributes.addAll(it)
                    VariableModel.newExercise.value.attributes = attributes
                },
                onDismiss = {
                    attributesExpanded = !attributesExpanded
                }
            )
            IconInput(
                selectedIcon = icon,
                onChange = { newIcon ->
                    icon = newIcon.toImageVector()
                    if (activity == "exercise") {
                        VariableModel.newExercise.value.icon = newIcon
                    }
                },
                color = C.LightBlue,
            )
            Connector(
                "exercise",
                ingredients,
                exercises,
                steps,
                onIngredientsChange = {
                    ingredients.clear()
                    ingredients.addAll(it)
                    VariableModel.newRecipe.value.ingredients = ingredients
                },
                onExercisesChange = {
                    exercises.clear()
                    exercises.addAll(it)
                    VariableModel.newExercise.value.exercises = exercises
                },
                onStepsChange = {
                    steps.clear()
                    steps.addAll(it)
                    VariableModel.newRecipe.value.steps = steps
                }
            )
        }
        ContinueButton(
            colorB = ButtonC.CoralRedPrimary,
            color = Color.White,
            enabled = true,
            onClickAction = {
                if (activity == "recipe") {
                    saveAction()
                } else {
                    saveAction()
                }
            },
            content = {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Save changes",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Save",
                        style = Typography.bodyLarge
                    )
                }
            }
        )
    }
}

@Composable
fun Connector(
    activity: String,
    ingredients: List<Ingredient>,
    exercises: List<SingleExercise>,
    steps: List<String>,
    onIngredientsChange: (List<Ingredient>) -> Unit,
    onExercisesChange: (List<SingleExercise>) -> Unit,
    onStepsChange: (List<String>) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("https://") }
    var video by remember { mutableStateOf("https://") }
    var amount by remember { mutableDoubleStateOf(1.0) }

    fun onAddIngredient(item: Ingredient, add: Boolean) {
        val updated = if (add) {
            ingredients + item
        } else {
            ingredients - item
        }
        onIngredientsChange(updated)
    }
    fun onAddExercise(item: SingleExercise, add: Boolean) {
        val updated = if (add) {
            exercises + item
        } else {
            exercises - item
        }
        onExercisesChange(updated)
    }
    fun onAddSteps(item: String, add: Boolean) {
        val updated = if (add) {
            steps + item
        } else {
            steps - item
        }
        onStepsChange(updated)
    }

    val collapseContent = @Composable { index: Int, isAdded: Boolean, expanded: Boolean ->
        CollapseContent(
            expanded = expanded,
            content = {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isAdded) C.GoldenAmber else Color.White)
                        .border(
                            width = 1.dp,
                            color = C.GoldenAmber,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    DefaultInput(
                        name = name,
                        onChange = { newName ->
                            name = newName
                            if (activity == "recipe") {
                                VariableModel.newIngredients[index].name = newName
                            } else {
                                VariableModel.newSingleExercises[index].name = newName
                            }
                        },
                        color = C.Red,
                        placeholder = "New $activity",
                        title = "Name"
                    )
                    if (activity == "recipe") {
                        DefaultInput(
                            name = amount.toString(),
                            onChange = { newAmount ->
                                amount = newAmount.toDouble()
                                VariableModel.newIngredients[index].amount = newAmount.toDouble()
                            },
                            color = C.Red,
                            placeholder = "0",
                            title = "Amount",
                            onlyInt = true
                        )
                    } else {
                        Description(
                            description = desc,
                            onChange = { newDesc ->
                                desc = newDesc
                                VariableModel.newSingleExercises[index].description = newDesc
                            },
                            color = C.Red
                        )
                        TimeInput(
                            time = time,
                            onChange = { newTime ->
                                time = newTime
                                VariableModel.newSingleExercises[index].time = newTime.toInt()
                            },
                            color = C.Red,
                        )
                        DefaultInput(
                            name = image,
                            onChange = { newImage ->
                                image = newImage
                                VariableModel.newSingleExercises[index].image = newImage
                            },
                            color = C.Red,
                            placeholder = "https://",
                            title = "Image"
                        )
                        DefaultInput(
                            name = video,
                            onChange = { newVideo ->
                                video = newVideo
                                VariableModel.newSingleExercises[index].video = newVideo
                            },
                            color = C.Red,
                            placeholder = "https://",
                            title = "Video"
                        )
                    }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    if (activity == "recipe") {
        if (VariableModel.allIngredients.isNotEmpty()) {
            Column {
                Text(
                    text = "Example ingredients",
                    style = Typography.labelSmall
                )
                VariableModel.allIngredients.forEachIndexed { index, key ->
                    val item = VariableModel.allIngredients[index]
                    val isAdded = ingredients.contains(item)
//                    var isAdded by remember { mutableStateOf(ingredients.contains(item)) }
                    var expanded by remember { mutableStateOf(false) }

                    fun onAdd(newAdd: Boolean) {
                        onAddIngredient(item, newAdd)
                    }
                    Spacer(modifier = Modifier.padding(top = 8.dp))

                    Checkbox(
                        borderColor = Color.White,
                        backgroundColor = C.GoldenAmber,
                        checked = isAdded,
                        onCheckedChange = ::onAdd,
                        text = item.name,
                        isCheckbox = true
                    ) {
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = "Collapse",
                            tint = if (isAdded) Color.White else C.GoldenAmber,
                            modifier = Modifier
                                .clickable {
                                    expanded = !expanded
                                }
                        )
                    }
                    collapseContent(index, isAdded, expanded)
                }
            }
        }
        Column {
            Text (
                text = "Create your own ingredients",
                style = Typography.labelSmall
            )

            var total by remember { mutableIntStateOf(1) }
            for (i in 0 until total) {
                var item = VariableModel.newIngredients.getOrNull(i)
                var index: Int
                if (item == null) {
                    item = Ingredient("", 1.0, "")
                    VariableModel.newIngredients.add(item)
                    index = VariableModel.newIngredients.indexOf(item)
                } else {
                    index = i
                }
                val isAdded = ingredients.contains(item)
//                var isAdded by remember { mutableStateOf(ingredients.contains(item)) }
                var expanded by remember { mutableStateOf(false) }

                fun onAdd(newAdd: Boolean) {
                    onAddIngredient(item, newAdd)
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))

                Checkbox(
                    borderColor = Color.White,
                    backgroundColor = C.GoldenAmber,
                    checked = isAdded,
                    onCheckedChange = ::onAdd,
                    text = item.name.ifEmpty { "New ingredient" },
                    isCheckbox = true
                ) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Collapse",
                        tint = if (isAdded) Color.White else C.GoldenAmber,
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            }
                    )
                }
                collapseContent(index, isAdded, expanded)
            }
            ButtonPrimary(
                ButtonC.IndigoPrimary,
                C.Indigo,
                {
                    total ++
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                content = {
                    Text (
                        text = "Extra field",
                        style = Typography.bodyMedium,
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
            var totalSteps by remember { mutableIntStateOf(1) }
            for (i in 0 until total) {
                var item by remember { mutableStateOf(VariableModel.newRecipe.value.steps.getOrNull(i) ?: "") }
                val isAdded = steps.contains(item)

                fun onAdd(newAdd: Boolean) {
                    onAddSteps(item, newAdd)
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))

                OutlinedTextField(
                    value = item,
                    placeholder = { Text("Set step ${i + 1}") },
                    onValueChange = { newStep ->
                        item = newStep
                    },
                    colors = getTextFieldColor(Color.White, C.Red, Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    leadingIcon = {
                        Row (
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DragIndicator,
                                contentDescription = "Select attributes",
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${i + 1}",
                                    style = Typography.bodyMedium.copy(
                                        color = Color.Black
                                    )
                                )
                            }
                        }
                    }
                )
            }
            ButtonPrimary(
                ButtonC.IndigoPrimary,
                C.Indigo,
                {
                    total ++
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                content = {
                    Text (
                        text = "Extra step field",
                        style = Typography.bodyMedium,
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
    } else {
        Column {
            Text (
                text = "Add your own exercises",
                style = Typography.labelSmall
            )

            var total by remember { mutableIntStateOf(1) }
            for (i in 0 until total) {
                var item = VariableModel.newSingleExercises.getOrNull(i)
                var index: Int
                if (item == null) {
                    item = SingleExercise(id = null, name = "", description = "", time = 0, image = "https://", video = "https://")
                    VariableModel.newSingleExercises.add(item)
                    index = VariableModel.newSingleExercises.indexOf(item)
                } else {
                    index = i
                }
                println(item.name)
                val isAdded = exercises.contains(item)
//                var isAdded by remember { mutableStateOf(exercises.contains(item)) }
                var expanded by remember { mutableStateOf(false) }

                fun onAdd(newAdd: Boolean) {
                    onAddExercise(item, newAdd)
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))

                Checkbox(
                    borderColor = Color.White,
                    backgroundColor = C.GoldenAmber,
                    checked = isAdded,
                    onCheckedChange = ::onAdd,
                    text = item.name.ifEmpty { "New exercise" },
                    isCheckbox = true
                ) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Collapse",
                        tint = if (isAdded) Color.White else C.GoldenAmber,
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            }
                    )
                }
                collapseContent(index,isAdded, expanded)
            }
            ButtonPrimary(
                ButtonC.IndigoPrimary,
                C.Indigo,
                {
                    total ++
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                content = {
                    Text (
                        text = "Extra field",
                        style = Typography.bodyMedium,
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
}

@Composable
@Preview(showBackground = true)
fun ActivityEditorPreview() {
    ActivityEditor("recipe", {})
}