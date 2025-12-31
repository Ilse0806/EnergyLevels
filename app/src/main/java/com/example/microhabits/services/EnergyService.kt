package com.example.microhabits.services

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.toEnergyLevel
import java.time.LocalDate

object EnergyService {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getEnergyLevel(date: LocalDate, context : Context) {
        DatabaseService.getRow("energy_level", mapOf("date" to date, "fetch_one" to true), context,
            { energyLevel ->
                VariableModel.todayEnergy = mutableStateOf(energyLevel.toEnergyLevel())
                Log.d("GET_ENERGY_SUCCESS", energyLevel.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }
}