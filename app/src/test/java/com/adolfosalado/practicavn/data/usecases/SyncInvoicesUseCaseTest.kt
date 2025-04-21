package com.adolfosalado.practicavn.data.usecases

import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoicesResponse
import com.adolfosalado.practicavn.data.network.ApiService
import com.adolfosalado.practicavn.data.repository.InvoiceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class SyncInvoicesUseCaseTest {

    private lateinit var invoiceDao: InvoiceDao
    private lateinit var apiService: ApiService
    private lateinit var repository: InvoiceRepository
    private lateinit var useCase: SyncInvoicesUseCase

    @Before
    fun setup() {
        invoiceDao = mock()
        apiService = mock()
        repository = InvoiceRepository(invoiceDao, apiService)
        useCase = SyncInvoicesUseCase(repository)
    }

    @Test
    fun `invoke calls syncInvoices from repository`() = runTest {
        // Given
        val mockInvoices = listOf(
            Invoice("Pendiente", 100.0, "01/01/2023"),
            Invoice("Pagada", 200.0, "01/10/2023")
        )
        val mockResponse = InvoicesResponse(numFacturas = 2, facturas = mockInvoices)

        whenever(apiService.getInvoices()).thenReturn(mockResponse)
        whenever(invoiceDao.getInvoiceCount()).thenReturn(0)

        // When
        useCase.invoke()

        // Then
        verify(invoiceDao).deleteAllInvoices()
        verify(invoiceDao).insertInvoices(any())
    }
}
