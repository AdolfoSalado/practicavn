package com.adolfosalado.practicavn.data.repositories

import android.content.Context
import com.adolfosalado.practicavn.data.database.InvoiceDatabase
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.network.RetrofitInstance
import retrofit2.Response

class InvoiceRepository(context: Context) {
    private val db = InvoiceDatabase.getDatabase(context)
    private val invoiceDao = db.getInvoiceDao()
    private val apiService = RetrofitInstance.apiService

    suspend fun getAllInvoices(): List<Invoice> {
        return invoiceDao.getAllInvoices().map {
            Invoice(it.id, it.date, it.amount, it.status)
        }
    }

    suspend fun getInvoicesFromApi(): Response<List<Invoice>> {
        return apiService.getInvoices()
    }
}