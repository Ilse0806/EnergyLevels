package com.example.microhabits.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

object DatabaseService {
//    This function fetches the data inside of table, based on the table name that is passed through
    fun fetchTable(table: String, context: Context, onSuccess: (JSONArray) -> Unit, onError: (VolleyError) -> Unit) {
//        Based on the constant variable of the base url, it gets connected to the api I created and executes a GET call
        val url = "${DatabaseConstants.GET_TABLE_URL}$table"
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response -> onSuccess(response) },
            { error -> onError(error) }
        )
        Volley.newRequestQueue(context).add(request)
    }

//    Look at the following functions still, try to find a way to check whether a row already exists or not, if it does, then adjust it instead of adding a new row
//    Same goes for the delete functions, first check if the row actually exists before trying to delete a row
    fun updateRow(table: String, data: Map<String, Any>, context: Context, onSuccess: (JSONObject) -> Unit, onError: (VolleyError) -> Unit) {
        val url = "${DatabaseConstants.UPDATE_URL}$table"
        val body = JSONObject(data)

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            { response -> onSuccess(response) },
            { error -> onError(error) }
        )

        Volley.newRequestQueue(context).add(request)
    }

    fun deleteRow(table: String, data: Map<String, Any>, context: Context, onSuccess: (JSONObject) -> Unit, onError: (VolleyError) -> Unit) {
        val url = "${DatabaseConstants.DELETE_ROW_URL}$table"
        val body = JSONObject(data)

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            { response -> onSuccess(response) },
            { error -> onError(error) }
        )

        Volley.newRequestQueue(context).add(request)
    }

    fun getRow(table: String, data: Map<String, Any>, context: Context, onSuccess: (JSONObject) -> Unit, onError: (VolleyError) -> Unit) {
        val url = "${DatabaseConstants.GET_ROW_URL}$table"
        val body = JSONObject(data)

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            { response -> onSuccess(response) },
            { error -> onError(error) }
        )
        Volley.newRequestQueue(context).add(request)
    }

    fun getId(table: String, data: Map<String, Any>, context: Context, onSuccess: (JSONObject) -> Unit, onError: (VolleyError) -> Unit) {
        val url = "${DatabaseConstants.GET_ID_URL}$table"
        val body = JSONObject(data)

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            { response ->
                if (response.has("id")) {
                    onSuccess(response)
                } else {
                    onError(VolleyError(response.optString("error", "Unknown error")))
                }
            },
            { error -> onError(error) }
        )

        Volley.newRequestQueue(context).add(request)
    }
}