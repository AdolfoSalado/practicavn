package com.adolfosalado.practicavn.data.usecases

import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.network.ApiService
import com.adolfosalado.practicavn.data.repository.InvoiceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetInvoicesUseCaseTest {

    @Mock
    private lateinit var invoiceDao: InvoiceDao

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var getInvoicesUseCase: GetInvoicesUseCase

    @Before
    fun setUp() {
        // Aquí se crea la instancia de InvoiceRepository con los mocks
        val repository = InvoiceRepository(invoiceDao, apiService)
        getInvoicesUseCase = GetInvoicesUseCase(repository)
    }

    @Test
    fun `invoke returns mapped invoices from repository`() = runTest {
        // Given
        val invoiceEntities = listOf(
            InvoiceEntity(1, 1672527600000, 100.0, "Pendiente"), // 1 de enero de 2023
            InvoiceEntity(2, 1696120800000, 200.0, "Pagada") // 1 de octubre de 2023
        )
        val expectedInvoices = listOf(
            Invoice("Pendiente", 100.0, "01/01/2023"),
            Invoice("Pagada", 200.0, "01/10/2023")
        )

        whenever(invoiceDao.getAllInvoices()).thenReturn(invoiceEntities)

        // When
        val result = getInvoicesUseCase()

        // Then
        assertThat(result).isEqualTo(expectedInvoices)
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        // Given
        val emptyList = emptyList<InvoiceEntity>()

        whenever(invoiceDao.getAllInvoices()).thenReturn(emptyList)

        // When
        val result = getInvoicesUseCase()

        // Then
        assertThat(result).isEqualTo(emptyList<Invoice>())
    }
}