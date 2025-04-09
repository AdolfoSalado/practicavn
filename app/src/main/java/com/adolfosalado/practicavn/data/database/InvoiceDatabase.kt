package com.adolfosalado.practicavn.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity

@Database(entities = [InvoiceEntity::class], version = 1, exportSchema = false)
abstract class InvoiceDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao
}