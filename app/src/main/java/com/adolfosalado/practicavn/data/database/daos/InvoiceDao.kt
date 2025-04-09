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
}