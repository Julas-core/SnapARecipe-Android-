package com.example.snaprecipe.data.remote

import com.example.snaprecipe.utils.AndroidCertUtils
import com.example.snaprecipe.utils.AppContextProvider
import com.example.snaprecipe.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .callTimeout(120, TimeUnit.SECONDS)
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