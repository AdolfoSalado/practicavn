package com.adolfosalado.practicavn.data.network

import com.adolfosalado.practicavn.data.models.InvoicesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/getInvoices")
    suspend fun getInvoices(): InvoicesResponse
}