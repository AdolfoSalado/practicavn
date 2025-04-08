package com.adolfosalado.practicavn.data.repositories

import android.content.Context
import com.adolfosalado.practicavn.data.database.InvoiceDatabase
import com.adolfosalado.practicavn.data.models.Invoice

class InvoiceRepository(context: Context) {
    private val db = InvoiceDatabase.getDatabase(context)
    private val invoiceDao = db.getInvoiceDao()

    suspend fun getAllInvoices(): List<Invoice> {
        return invoiceDao.getAllInvoices().map {
            Invoice(it.id, it.date, it.amount, it.status)
        }
    }


}