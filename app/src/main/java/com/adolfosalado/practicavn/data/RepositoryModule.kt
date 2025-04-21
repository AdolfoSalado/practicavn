package com.adolfosalado.practicavn.data

import com.adolfosalado.practicavn.data.database.daos.InvoiceDao
import com.adolfosalado.practicavn.data.network.ApiService
import com.adolfosalado.practicavn.data.network.DetailsService
import com.adolfosalado.practicavn.data.repository.DetailsRepository
import com.adolfosalado.practicavn.data.repository.InvoiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideInvoiceRepository(invoiceDao: InvoiceDao, apiService: ApiService): InvoiceRepository {
        return InvoiceRepository(invoiceDao, apiService)
    }

    @ViewModelScoped
    @Provides
    fun provideDetailsRepository(detailsService: DetailsService): DetailsRepository {
        return DetailsRepository(detailsService)
    }
}