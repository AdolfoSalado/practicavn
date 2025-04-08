package com.adolfosalado.practicavn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_table")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "status") var status: String
)