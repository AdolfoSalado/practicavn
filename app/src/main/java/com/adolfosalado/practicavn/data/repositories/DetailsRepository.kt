package com.adolfosalado.practicavn.data.repository

import com.adolfosalado.practicavn.data.models.SmartSolarDetails
import com.adolfosalado.practicavn.data.network.DetailsService
import retrofit2.Response

class DetailsRepository(private val detailsService: DetailsService) {
    suspend fun getSmartSolarDetails(): Response<SmartSolarDetails> = detailsService.getSmartSolarDetails()
}