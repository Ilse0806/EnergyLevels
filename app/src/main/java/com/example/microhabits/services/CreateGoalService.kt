package com.example.microhabits.services

import android.content.Context
import android.util.Log
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import org.json.JSONArray
import org.json.JSONObject

object CreateGoalService {
    fun loadCategory (context: Context){
        DatabaseService.fetchTable(
            "category", context,
            { categories ->
                saveExistingCategories(categories)
                Log.d("API_SUCCESS_CATEGORIES", categories.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }

    private fun saveExistingCategories(response: JSONArray) {
        val newItems = JSONObject()
        for (i in 0 until response.length()) {
            val category = response.getJSONObject(i)
            val id = category.getString("id")
            val name = category.getString("name")
            newItems.put(id, name)
        }
        VariableModel.existingCategories.value = newItems

    }

    fun saveCategory(context: Context) {
        val allCats = VariableModel.existingCategories.value.keys().asSequence().toList()

        val existingId = allCats.find { key ->
            VariableModel.existingCategories.value.getString(key) == VariableModel.categoryValue.value
        }

        if (existingId == null) {
            DatabaseService.updateRow(
                "category", mapOf("name" to VariableModel.categoryValue.value), context,
                { savedCategory ->
                    VariableModel.goalCategory.value.put("id", savedCategory.get("id"))
                    VariableModel.goalCategory.value.put("name", VariableModel.categoryValue.value)
                    Log.d("API_SUCCESS", savedCategory.toString())
                },
                { error -> Log.e("API_ERROR", error.toString()) }
            )
        } else {
            VariableModel.goalCategory.value.put("id", existingId.toInt())
            VariableModel.goalCategory.value.put("name", VariableModel.categoryValue.value)
        }
    }
}