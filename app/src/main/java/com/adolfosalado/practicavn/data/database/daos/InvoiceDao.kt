package com.adolfosalado.practicavn.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import com.adolfosalado.practicavn.data.models.Invoice

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoice_table")
    suspend fun getAllInvoices(): List<InvoiceEntity>

    @Insert
    suspend fun insertInvoices(invoices: List<InvoiceEntity>)

    @Query("SELECT COUNT(*) FROM invoice_table")
    suspend fun getInvoiceCount(): Int

    @Query("DELETE FROM invoice_table")
    suspend fun deleteAllInvoices()

    @Query("SELECT MIN(amount) FROM invoice_table")
    suspend fun getImporteMin(): Double

    @Query("SELECT MAX(amount) FROM invoice_table")
    suspend fun getImporteMax(): Double

    @Query("SELECT * FROM invoice_table WHERE status = :status")
    suspend fun getInvoicesByStatus(status: String): List<InvoiceEntity>

    @Query("SELECT * FROM invoice_table WHERE date BETWEEN :from AND :to")
    suspend fun getInvoicesByDate(from: Long, to: Long): List<InvoiceEntity>

    @Query("SELECT DISTINCT status FROM invoice_table")
    suspend fun getDistinctStatuses(): List<String>

    @Query("""
    SELECT * FROM invoice_table
    WHERE (:dateFrom IS NULL OR date >= :dateFrom)
    AND (:dateTo IS NULL OR date <= :dateTo)
    AND (:amount IS NULL OR amount BETWEEN 0 AND :amount)
    AND (:statusListSize = 0 OR status IN (:statusList))
""")
    suspend fun getFilteredInvoices(
        dateFrom: Long? = null,
        dateTo: Long? = null,
        amount: Double? = null,
        statusList: List<String> = emptyList(),
        statusListSize: Int = 0
    ): List<InvoiceEntity>


    @Query(
        """
    SELECT * FROM invoice_table WHERE
    (:dateFrom IS NULL OR date >= :dateFrom) AND
    (:dateTo IS NULL OR date <= :dateTo) AND
    (:amount IS NULL OR amount = :amount) AND
    status IN (:statusList)
    """
    )
    suspend fun getFilteredInvoicesWithStatus(
        dateFrom: Long? = null,
        dateTo: Long? = null,
        amount: Double? = null,
        statusList: List<String>
    ): List<InvoiceEntity>
}