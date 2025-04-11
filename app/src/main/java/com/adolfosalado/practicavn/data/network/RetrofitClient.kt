package com.adolfosalado.practicavn.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://e128693c-0c8c-4aae-806f-07cd232bd8f3.mock.pstmn.io"
    private const val BASE_URL_DETAILS =
        "https://e83d2089-e9b0-4182-a9cd-ca2e8f768049.mock.pstmn.io"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val apiDetails: DetailsService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_DETAILS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DetailsService::class.java)
    }


}