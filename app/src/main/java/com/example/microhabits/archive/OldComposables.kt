package com.example.microhabits.archive

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