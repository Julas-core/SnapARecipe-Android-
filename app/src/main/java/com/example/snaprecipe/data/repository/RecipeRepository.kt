package com.example.snaprecipe.data.repository

import com.example.snaprecipe.data.remote.RetrofitClient
import com.example.snaprecipe.utils.Constants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class RecipeRepository {

    suspend fun analyzeImage(base64Image: String): String {

        val json = """
        {
          "contents": [{
            "parts": [
              {
                "text": "
                You are a professional chef.

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

        return response.body()?.string() ?: "No response"
    }
}