interface ApiService {

    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun analyzeImage(
        @Query("key") apiKey: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}