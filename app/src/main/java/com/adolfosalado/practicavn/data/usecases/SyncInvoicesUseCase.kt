package com.adolfosalado.practicavn.data.usecases

import com.adolfosalado.practicavn.data.repository.InvoiceRepository

class SyncInvoicesUseCase(private val repository: InvoiceRepository) {
    suspend operator fun invoke() {
        repository.syncInvoices()
    }
}