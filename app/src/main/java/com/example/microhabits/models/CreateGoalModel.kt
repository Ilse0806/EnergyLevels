package com.example.microhabits.models

import android.content.Context
import android.util.Log
import com.example.microhabits.api.DatabaseService
import org.json.JSONArray
import org.json.JSONObject

object CreateGoalModel {
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
        for (key in allCats) {
            val category = VariableModel.existingCategories.value.getString(key)
            if (VariableModel.categoryValue.value != category) {
                DatabaseService.updateRow(
                    "category", mapOf("name" to VariableModel.categoryValue.value), context,
                    { savedCategory ->
                        Log.d("API_SUCCESS", savedCategory.toString())
                    },
                    { error -> Log.e("API_ERROR", error.toString()) }
                )
                break
            }
        }
    }
}