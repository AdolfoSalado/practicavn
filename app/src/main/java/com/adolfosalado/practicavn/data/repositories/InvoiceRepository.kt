package com.adolfosalado.practicavn.data.repositories


import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoiceFilter

class InvoiceRepository(private val invoiceDao: InvoiceDao) {

    suspend fun getInvoicesFiltered(
        filter: InvoiceFilter
    ): List<InvoiceEntity> {
        return invoiceDao.getFilteredInvoices(
            dateFrom = filter.dateFrom,
            dateTo = filter.dateTo,
            amount = filter.amount as Double?,
            statusList = filter.statusList
        )
    }

}