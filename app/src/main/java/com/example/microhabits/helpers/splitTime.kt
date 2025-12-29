package com.example.microhabits.helpers

fun splitTime(time: Int): Map<String, Int> {
    val hours = time / 60
    val minutes = time % 60

    return mapOf("minutes" to minutes, "hours" to hours)
}