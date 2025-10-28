package com.example.microhabits.archive

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject

@Composable
fun ArchivedSixColumnGrid(name: String, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(4) }) {
            Box {
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
            }
        }
        item(span = { GridItemSpan(2) }) {
            Box {
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun ArchivedStyles(modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Title",
            modifier = modifier,
            style = Typography.titleLarge,
        )
        Text(
            text = "Heading 1",
            modifier = modifier,
            style = Typography.titleMedium,
        )
        Text(
            text = "Heading 2",
            modifier = modifier,
            style = Typography.titleSmall,
        )
        Text(
            text = "Paragraph",
            modifier = modifier,
            style = Typography.bodyLarge,
        )
        Text(
            text = "Button",
            modifier = modifier,
            style = Typography.bodyMedium,
        )
        Text(
            text = "Caption",
            modifier = modifier,
            style = Typography.labelSmall,
        )
    }
}

//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 4.dp, bottom = 4.dp),
//                shape = RoundedCornerShape(8.dp),
//                onClick = { behaviorDetails(context, Intent(context, DisplayBehavior::class.java), behavior["id"] as Int, behavior) },
//                colors = ButtonC.GoldenAmberPrimary,
//                border = borderStroke(C.GoldenAmber),
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp)
//                    ) {
//                        Text(
//                            text = behavior["name"].toString(),
//                            style = Typography.bodyLarge,
//                        )
//                        Text(
//                            text = behavior["description"].toString(),
//                            style = Typography.labelSmall,
//                        )
//                    }
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                        contentDescription = "Fav",
//                        tint = C.Indigo,
//                    )
//                }
//            }

fun buttonClicked(context: Context, intent: Intent) {
    context.startActivity(intent)
}

fun behaviorDetails(context: Context, intent: Intent, id: Int, fullBehavior: Map<String, Any?>) {
    intent.putExtra("behavior_id", id)
    intent.putExtra("behavior", JSONObject(fullBehavior).toString())
    context.startActivity(intent)
}