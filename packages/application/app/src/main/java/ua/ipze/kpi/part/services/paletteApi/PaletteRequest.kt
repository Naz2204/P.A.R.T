package ua.ipze.kpi.part.services.paletteApi

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object PaletteRequest {
    private val client = OkHttpClient()

    suspend fun fetchPalette(): List<List<Int>> =
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

            val json = """
                {
                  "model": "default"
                }
            """

            val body = json.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("http://colormind.io/api/")
                .post(body)
                .build()
            Log.d("fetch", request.toString())
            client.newCall(request).execute().use { response ->
                val jsonString = response.body?.string()
                    ?: error("Empty response")

                val resultArray = JSONObject(jsonString).getJSONArray("result")

                List(resultArray.length()) { i ->
                    val c = resultArray.getJSONArray(i)
                    listOf(c.getInt(0), c.getInt(1), c.getInt(2))
                }
            }
        }
}
