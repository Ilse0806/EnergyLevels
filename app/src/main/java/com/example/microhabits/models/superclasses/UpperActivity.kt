package com.example.microhabits.models.superclasses

import com.example.microhabits.models.classes.NavigationOption

open class UpperActivity (
    id: Int?,
    name: String,
    description: String,
    time: Int,
    var difficulty: Int,
    var attributes: List<String>,
): Activity(id, name, description, time) {
    open fun toNavigationOption(): NavigationOption<*> {
        return NavigationOption(
            label = this.name,
            destination = this,
        )
    }
}