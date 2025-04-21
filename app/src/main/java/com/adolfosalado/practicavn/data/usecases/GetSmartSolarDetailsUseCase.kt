package com.adolfosalado.practicavn.data.usecases

import com.adolfosalado.practicavn.data.models.SmartSolarDetails
import com.adolfosalado.practicavn.data.repository.DetailsRepository

class GetSmartSolarDetailsUseCase(private val repository: DetailsRepository) {
    suspend operator fun invoke(): SmartSolarDetails? {
        val response = repository.getSmartSolarDetails()
        if (response.isSuccessful) {
            return response.body()
        } else {
            return null
        }
    }
}