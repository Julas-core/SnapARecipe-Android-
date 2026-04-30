package com.example.snaprecipe.data.remote

import com.example.snaprecipe.utils.AndroidCertUtils
import com.example.snaprecipe.utils.AppContextProvider
import com.example.snaprecipe.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val context = AppContextProvider.get()
            val sha1 = AndroidCertUtils.getSignatureSha1(context)
            val request = chain.request().newBuilder()
                .addHeader("X-Android-Package", context.packageName)
                .addHeader("X-Android-Cert", sha1)
                .build()
            chain.proceed(request)
        }
        .build()

    val api: ApiService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}