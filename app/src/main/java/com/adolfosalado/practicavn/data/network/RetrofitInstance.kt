package com.adolfosalado.practicavn.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://e128693c-0c8c-4aae-806f-07cd232bd8f3.mock.pstmn.io/"

    private val client = OkHttpClient.Builder().build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)

    }

}