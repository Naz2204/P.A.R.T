package ua.ipze.kpi.part.services.paletteApi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class PaletteViewModel : ViewModel() {

    var colors: List<List<Int>> = emptyList()

    fun fetchColors(
        onResult: (List<List<Int>>) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        Log.d("fetch", "start")
        viewModelScope.launch {
            try {
                Log.d("fetch", "inside")
                colors = PaletteRequest.fetchPalette()
                Log.d("fetch", colors.toString())
                onResult(colors)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}

fun colorSchemeToJson(colors: List<List<Int>>): String {
    val jsonObject = JSONObject()
    val colorsArray = JSONArray()
    colors.forEach { color ->
        val colorArray = JSONArray()
        color.forEach { colorArray.put(it) }
        colorsArray.put(colorArray)
    }
    jsonObject.put("colors", colorsArray)
    return jsonObject.toString()
}
