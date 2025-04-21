package com.adolfosalado.practicavn.data

import com.adolfosalado.practicavn.data.network.ApiService
import com.adolfosalado.practicavn.data.network.DetailsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL_INVOICES = "https://e128693c-0c8c-4aae-806f-07cd232bd8f3.mock.pstmn.io/" // URL para facturas
    private const val BASE_URL_DETAILS = "https://e83d2089-e9b0-4182-a9cd-ca2e8f768049.mock.pstmn.io" // URL para detalles

    // Instancia de Retrofit para las facturas
    @Singleton
    @Provides
    @Named("Invoices")
    fun provideInvoicesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_INVOICES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Instancia de Retrofit para los detalles
    @Singleton
    @Provides
    @Named("Details")
    fun provideDetailsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_DETAILS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ApiService para las facturas
    @Singleton
    @Provides
    fun provideApiService(@Named("Invoices") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // DetailsService para los detalles
    @Singleton
    @Provides
    fun provideDetailsService(@Named("Details") retrofit: Retrofit): DetailsService {
        return retrofit.create(DetailsService::class.java)
    }
}