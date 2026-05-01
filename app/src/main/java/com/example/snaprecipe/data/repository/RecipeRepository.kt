package com.example.snaprecipe.data.repository

import com.example.snaprecipe.data.model.RecipeCard
import com.example.snaprecipe.data.remote.RetrofitClient
import com.example.snaprecipe.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class RecipeRepository {

    suspend fun analyzeImage(base64Image: String, language: String, languageCode: String): RecipeCard {

        val json = """
        {
          "contents": [{
            "parts": [
              {
                "text": "You are a professional chef. Respond only in $language ($languageCode).\nReturn ONLY valid JSON. Do not include markdown or extra text.\n\nJSON format:\n{\"title\":\"string\",\"intro\":\"short intro string\",\"instructions\":[\"step 1\",\"step 2\"],\"nutrition\":\"short nutrition summary string\",\"tips\":[\"tip 1\",\"tip 2\"],\"servings\":\"servings string\",\"time\":\"time string\"}"
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
            Constants.GEMINI_API_VERSION,
            Constants.GEMINI_MODEL,
            Constants.API_KEY,
            body
        )

        if (!response.isSuccessful) {
          val errorBody = response.errorBody()?.string()
          throw IllegalStateException("API error ${response.code()}: ${errorBody ?: "Unknown error"}")
        }

        val payload = response.body()?.string() ?: throw IllegalStateException("Empty response body")
        val modelText = extractText(payload) ?: throw IllegalStateException("Missing model text")
        val jsonBlock = extractJsonBlock(modelText) ?: modelText
        return Gson().fromJson(jsonBlock, RecipeCard::class.java)
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

    private fun extractJsonBlock(text: String): String? {
      val start = text.indexOf('{')
      val end = text.lastIndexOf('}')
      if (start == -1 || end == -1 || end <= start) {
        return null
      }

      return text.substring(start, end + 1)
    }
}
