package com.adolfosalado.practicavn.data.network


import com.adolfosalado.practicavn.data.models.SmartSolarDetails
import retrofit2.Response
import retrofit2.http.GET

interface DetailsService {
    @GET("getSmartSolarDetails")
    suspend fun getSmartSolarDetails(): Response<SmartSolarDetails>
}