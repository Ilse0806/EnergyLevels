package com.example.microhabits.services

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.toEnergyLevel
import java.time.LocalDate

object EnergyService {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getEnergyLevel(date: LocalDate, context : Context) {
        DatabaseService.getRow("energy_level", mapOf("user_id" to VariableModel.userId, "date" to date, "fetch_one" to true), context,
            { energyLevel ->
                VariableModel.todayEnergy.value = energyLevel.toEnergyLevel()
                Log.d("GET_ENERGY_SUCCESS", energyLevel.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }

    fun saveEnergyLevel(energyLevel: Map<String, Any?>, context: Context) {
        val newMap = energyLevel + mapOf("user_id" to VariableModel.userId)
        DatabaseService.updateRow("energy_level", newMap, context,
            { response ->
                Log.d("SAVE_ENERGY_LEVEL", response.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }
}