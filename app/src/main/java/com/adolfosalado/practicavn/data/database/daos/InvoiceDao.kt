package com.adolfosalado.practicavn.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity

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
    suspend fun getImporteMin(): Float

    @Query("SELECT MAX(amount) FROM invoice_table")
    suspend fun getImporteMax(): Float

    @Query("SELECT * FROM invoice_table WHERE status = :status")
    suspend fun getInvoicesByStatus(status: String): List<InvoiceEntity>

    @Query("SELECT * FROM invoice_table WHERE date BETWEEN :from AND :to")
    suspend fun getInvoicesByDate(from: Long, to: Long): List<InvoiceEntity>

    @Query("SELECT DISTINCT status FROM invoice_table")
    suspend fun getDistinctStatuses(): List<String>
}