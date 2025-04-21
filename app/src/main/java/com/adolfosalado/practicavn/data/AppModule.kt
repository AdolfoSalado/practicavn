package com.adolfosalado.practicavn.data

import android.app.Application
import androidx.room.Room
import com.adolfosalado.practicavn.data.database.InvoiceDatabase
import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideInvoiceDatabase(app: Application): InvoiceDatabase {
        return Room.databaseBuilder(
            app,
            InvoiceDatabase::class.java,
            "invoice_database"
        ).build()
    }

    @Provides
    fun provideInvoiceDao(db: InvoiceDatabase): InvoiceDao {
        return db.invoiceDao()
    }
}