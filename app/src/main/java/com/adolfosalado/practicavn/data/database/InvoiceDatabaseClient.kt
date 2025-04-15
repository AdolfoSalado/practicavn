package com.adolfosalado.practicavn.data.database

import android.content.Context
import androidx.room.Room

object InvoiceDatabaseClient {

    fun getDatabase(context: Context): InvoiceDatabase {
        return Room.databaseBuilder(
            context,
            InvoiceDatabase::class.java,
            "invoice_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}