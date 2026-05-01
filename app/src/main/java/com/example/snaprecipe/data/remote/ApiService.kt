package com.example.snaprecipe.data.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("{apiVersion}/models/{model}:generateContent")
    suspend fun analyzeImage(
        @Path("apiVersion") apiVersion: String,
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}
