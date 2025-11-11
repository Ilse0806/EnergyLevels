package com.example.microhabits.models

enum class MeasuredInResult(val value: String) {
    AMOUNT_OF_TIMES("amount of times"),
    TIME("time");

    companion object {
        fun fromInput(input: String): MeasuredInResult {
            return when (input.lowercase()) {
                "amount of times", "measured in" -> AMOUNT_OF_TIMES
                "time" -> TIME
                else -> throw IllegalArgumentException("Invalid frequency: $input")
            }
        }
    }
}