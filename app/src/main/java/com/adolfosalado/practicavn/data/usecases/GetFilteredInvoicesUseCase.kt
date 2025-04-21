package com.adolfosalado.practicavn.data.usecases

import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.repository.InvoiceRepository
import java.text.SimpleDateFormat
import java.util.Locale

class GetFilteredInvoicesUseCase(private val repository: InvoiceRepository) {
    suspend operator fun invoke(filter: InvoiceFilter): List<Invoice> {
        return repository.getFilteredInvoices(filter).map { mapEntityToInvoice(it) }
    }

    private fun mapEntityToInvoice(entity: com.adolfosalado.practicavn.data.database.entities.InvoiceEntity): Invoice {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return Invoice(entity.status, entity.amount, dateFormat.format(entity.date))
    }
}