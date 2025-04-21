package com.adolfosalado.practicavn.data.repository

import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.models.InvoicesResponse
import com.adolfosalado.practicavn.data.network.ApiService
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat
import java.util.Locale

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class InvoiceRepositoryTest {

    @Mock
    private lateinit var invoiceDao: InvoiceDao

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var invoiceRepository: InvoiceRepository

    @Before
    fun setUp() {
        invoiceRepository = InvoiceRepository(invoiceDao, apiService)
    }

    @Test
    fun `getAllInvoices returns data from DAO`() = runTest {
        // Given
        val invoices = listOf(
            InvoiceEntity(1, 1672527600000, 100.0, "Pendiente"), // 1 de enero de 2023
            InvoiceEntity(2, 1696120800000, 200.0, "Pagada") // 1 de octubre de 2023
        )
        whenever(invoiceDao.getAllInvoices()).thenReturn(invoices)

        // When
        val result = invoiceRepository.getAllInvoices()

        // Then
        Truth.assertThat(result).isEqualTo(invoices)
    }

    @Test
    fun `getFilteredInvoices returns data from DAO`() = runTest {
        // Given
        val filter = InvoiceFilter(
            dateFrom = 1672527600000,
            dateTo = 1696120800000,
            amount = 100.0,
            statusList = listOf("Pendiente")
        )
        val invoices = listOf(
            InvoiceEntity(1, 1672527600000, 100.0, "Pendiente"), // 1 de enero de 2023
            InvoiceEntity(2, 1696120800000, 200.0, "Pagada") // 1 de octubre de 2023
        )

        whenever(
            invoiceDao.getFilteredInvoices(
                dateFrom = filter.dateFrom,
                dateTo = filter.dateTo,
                amount = filter.amount,
                statusList = filter.statusList,
                statusListSize = filter.statusList?.size ?: 0
            )
        ).thenReturn(invoices)

        // When
        val result = invoiceRepository.getFilteredInvoices(filter)

        // Then
        Truth.assertThat(result).isEqualTo(invoices)
    }

    @Test
    fun `getInvoiceCount returns data from DAO`() = runTest {
        //Given
        val count = 2
        whenever(invoiceDao.getInvoiceCount()).thenReturn(count)

        //When
        val result = invoiceRepository.getInvoiceCount()

        //Then
        Truth.assertThat(result).isEqualTo(count)
    }

    @Test
    fun `getImporteMax returns data from DAO`() = runTest {
        //Given
        val importeMax = 200.0
        whenever(invoiceDao.getImporteMax()).thenReturn(importeMax)

        //When
        val result = invoiceRepository.getImporteMax()

        //Then
        Truth.assertThat(result).isEqualTo(importeMax)
    }

    @Test
    fun `getDistinctStatuses returns data from DAO`() = runTest {
        //Given
        val statuses = listOf("Pendiente", "Pagada")
        whenever(invoiceDao.getDistinctStatuses()).thenReturn(statuses)

        //When
        val result = invoiceRepository.getDistinctStatuses()

        //Then
        Truth.assertThat(result).isEqualTo(statuses)
    }

    @Test
    fun `syncInvoices inserts data from API when apiNumberInvoices is different from roomNumberInvoices`() =
        runTest {
            // Given
            val invoices = listOf(
                //Se ha cambiado la fecha
                Invoice("Pendiente", 100.0, "01/01/2023"),
                Invoice("Pagada", 100.0, "01/01/2023"),
            )
            // Ahora se utiliza InvoicesResponse
            val apiResponse = InvoicesResponse(invoices.size, invoices)
            whenever(apiService.getInvoices()).thenReturn(apiResponse)
            whenever(invoiceDao.getInvoiceCount()).thenReturn(0)

            // When
            invoiceRepository.syncInvoices()

            // Then
            verify(invoiceDao).deleteAllInvoices()
            verify(invoiceDao).insertInvoices(invoices.map { invoice ->
                InvoiceEntity(
                    0,
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(invoice.date)?.time
                        ?: 0L,
                    invoice.amount,
                    invoice.status
                )
            })
        }

    @Test
    fun `syncInvoices does not insert data from API when apiNumberInvoices is equal to roomNumberInvoices`() =
        runTest {
            // Given
            val invoices = listOf(
                Invoice("Pendiente", 100.0, "01/01/2023"),
                Invoice("Pagada", 100.0, "01/01/2023"),
            )
            val apiResponse = InvoicesResponse(invoices.size, invoices)
            whenever(apiService.getInvoices()).thenReturn(apiResponse)
            whenever(invoiceDao.getInvoiceCount()).thenReturn(2)

            // When
            invoiceRepository.syncInvoices()

            // Then
            verify(invoiceDao, never()).deleteAllInvoices()
            verify(invoiceDao, never()).insertInvoices(invoices.map { invoice ->
                InvoiceEntity(
                    0,
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(invoice.date)?.time
                        ?: 0L,
                    invoice.amount,
                    invoice.status
                )
            })
        }

    @Test
    fun `deleteAllInvoices calls deleteAllInvoices from DAO`() = runTest {
        // When
        invoiceRepository.deleteAllInvoices()

        // Then
        verify(invoiceDao).deleteAllInvoices()
    }

    @Test
    fun `insertInvoices calls insertInvoices from DAO`() = runTest {
        // Given
        val invoices = listOf(
            Invoice("Pendiente", 100.0, "01/01/2023"),
            Invoice("Pagada", 100.0, "01/01/2023"),
        )

        // When
        invoiceRepository.insertInvoices(invoices)

        // Then
        verify(invoiceDao).insertInvoices(invoices.map { invoice ->
            InvoiceEntity(
                0,
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(invoice.date)?.time ?: 0L,
                invoice.amount,
                invoice.status
            )
        })
    }
}