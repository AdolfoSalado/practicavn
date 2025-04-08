package com.adolfosalado.practicavn.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity

@Database(entities = [InvoiceEntity::class], version = 1)
abstract class InvoiceDatabase : RoomDatabase() {

    abstract fun getInvoiceDao(): InvoiceDao

    companion object {
        fun getDatabase(context: Context): InvoiceDatabase {
            return Room.databaseBuilder(
                context,
                InvoiceDatabase::class.java,
                "invoice_database"
            ).build()
        }
    }
}