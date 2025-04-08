package com.adolfosalado.practicavn.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity

@Dao
interface InvoiceDao {

    @Query("SELECT * FROM invoice_table")
    suspend fun getAllInvoices(): List<InvoiceEntity>
}