package com.adolfosalado.practicavn.data

import com.adolfosalado.practicavn.data.repository.DetailsRepository
import com.adolfosalado.practicavn.data.repository.InvoiceRepository
import com.adolfosalado.practicavn.data.usecases.GetFilteredInvoicesUseCase
import com.adolfosalado.practicavn.data.usecases.GetInvoicesUseCase
import com.adolfosalado.practicavn.data.usecases.GetSmartSolarDetailsUseCase
import com.adolfosalado.practicavn.data.usecases.SyncInvoicesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideGetInvoicesUseCase(invoiceRepository: InvoiceRepository): GetInvoicesUseCase {
        return GetInvoicesUseCase(invoiceRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetFilteredInvoicesUseCase(invoiceRepository: InvoiceRepository): GetFilteredInvoicesUseCase {
        return GetFilteredInvoicesUseCase(invoiceRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideSyncInvoicesUseCase(invoiceRepository: InvoiceRepository): SyncInvoicesUseCase {
        return SyncInvoicesUseCase(invoiceRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetSmartSolarDetailsUseCase(detailsRepository: DetailsRepository): GetSmartSolarDetailsUseCase {
        return GetSmartSolarDetailsUseCase(detailsRepository)
    }
}