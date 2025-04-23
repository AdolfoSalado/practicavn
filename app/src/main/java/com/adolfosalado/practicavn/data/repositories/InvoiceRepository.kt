package com.adolfosalado.practicavn.data.repository

import android.util.Log
import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.network.ApiService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

open class InvoiceRepository(
    private val invoiceDao: InvoiceDao,
    private val apiService: ApiService
) {

    suspend fun getAllInvoices(): List<InvoiceEntity> = invoiceDao.getAllInvoices()

    suspend fun getFilteredInvoices(filter: InvoiceFilter): List<InvoiceEntity> {
        val adjustedDateTo = filter.dateTo?.let {
            Calendar.getInstance().apply {
                timeInMillis = it
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis
        }

        return invoiceDao.getFilteredInvoices(
            dateFrom = filter.dateFrom,
            dateTo = adjustedDateTo,
            amount = filter.amount,
            statusList = filter.statusList ?: emptyList(),
            applyStatusList = if ((filter.statusList?.isNotEmpty() == true)) 1 else 0
        )
    }


    suspend fun getInvoiceCount(): Int = invoiceDao.getInvoiceCount()

    suspend fun getImporteMax(): Double = invoiceDao.getImporteMax()

    suspend fun getDistinctStatuses(): List<String> = invoiceDao.getDistinctStatuses()

    suspend fun syncInvoices() {
        val apiResponse = apiService.getInvoices()

        val apiNumberInvoices = apiResponse.numFacturas?.toInt() ?: 0
        val roomNumberInvoices = invoiceDao.getInvoiceCount()

        if (apiNumberInvoices != roomNumberInvoices) {
            invoiceDao.deleteAllInvoices()
            invoiceDao.insertInvoices(apiResponse.facturas.map { mapInvoiceToEntity(it) })
        }
    }

    suspend fun deleteAllInvoices() = invoiceDao.deleteAllInvoices()

    suspend fun insertInvoices(invoices: List<Invoice>) {
        invoiceDao.insertInvoices(invoices.map { mapInvoiceToEntity(it) })
    }

    private fun mapInvoiceToEntity(invoice: Invoice): InvoiceEntity {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimestamp = dateFormat.parse(invoice.date)?.time ?: 0L
        val roundedAmount = "%.2f".format(Locale.US, invoice.amount).toDouble()
        return InvoiceEntity(0, dateTimestamp, roundedAmount, invoice.status)
    }
}
