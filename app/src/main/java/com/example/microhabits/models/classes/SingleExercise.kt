package com.example.microhabits.models.classes

import com.example.microhabits.models.superclasses.Activity

class SingleExercise(
    id: Int,
    name: String,
    description: String,
    time: Int,
    var image: String,
    var video: String,
): Activity(id, name, description, time) {
}