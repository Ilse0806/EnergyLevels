package com.example.microhabits.models.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

class FilterItem<T> (
    var name: String,
    selected: Boolean = false,
    var singleSelect: Boolean = true,
    val favoritesList: List<NavigationOption<*>>,
    val recommendedList: SnapshotStateList<T>
) {
    var selected by mutableStateOf(selected)
}