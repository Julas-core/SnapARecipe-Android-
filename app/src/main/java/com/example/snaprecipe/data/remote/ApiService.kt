package com.example.snaprecipe.data.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("v1/models/gemini-3.0-flash:generateContent")
    suspend fun analyzeImage(
        @Query("key") apiKey: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}