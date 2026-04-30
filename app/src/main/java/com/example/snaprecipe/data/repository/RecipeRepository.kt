package com.example.snaprecipe.data.repository

import com.example.snaprecipe.data.remote.RetrofitClient
import com.example.snaprecipe.utils.Constants
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class RecipeRepository {

    suspend fun analyzeImage(base64Image: String, language: String, languageCode: String): String {

        val json = """
        {
          "contents": [{
            "parts": [
              {
                "text": "
                You are a professional chef. Respond only in $language ($languageCode).

1. Identify the dish
2. List ingredients with exact quantities
3. Provide clear step-by-step instructions
4. Suggest 1 variation.
"
              },
              {
                "inline_data": {
                  "mime_type": "image/jpeg",
                  "data": "$base64Image"
                }
              }
            ]
          }]
        }
        """

        val body = json.toRequestBody("application/json".toMediaType())

        val response = RetrofitClient.api.analyzeImage(
            Constants.API_KEY,
            body
        )

        val payload = response.body()?.string() ?: return "No response"
        return extractText(payload) ?: payload
    }

    private fun extractText(payload: String): String? {
        return try {
            val root = JsonParser.parseString(payload).asJsonObject
            val candidates = root.getAsJsonArray("candidates")
            if (candidates == null || candidates.size() == 0) {
                return null
            }

            val content = candidates[0].asJsonObject.getAsJsonObject("content")
            val parts = content?.getAsJsonArray("parts")
            if (parts == null || parts.size() == 0) {
                return null
            }

            parts[0].asJsonObject.get("text")?.asString
        } catch (_: Exception) {
            null
        }
    }
}