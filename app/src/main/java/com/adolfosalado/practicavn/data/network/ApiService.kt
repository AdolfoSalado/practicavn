package com.adolfosalado.practicavn.data.network

import com.adolfosalado.practicavn.data.models.Invoice
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/getInvoices")
    suspend fun getInvoices(): Response<List<Invoice>>
}