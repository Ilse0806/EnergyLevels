package com.example.microhabits.models.classes

import java.time.LocalDateTime

class Goal(
    var id: Int?,
    var name: String,
    var description: String?,
    var deadline: LocalDateTime?,
    var category: String?
) {
}