package com.adolfosalado.practicavn.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.database.InvoiceDatabaseClient
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class InvoiceViewModel(application: Application) : AndroidViewModel(application) {
    private val database = InvoiceDatabaseClient.getDatabase(application)
    private val invoiceDao = database.invoiceDao()
    private val api = RetrofitClient.api

    val invoicesLiveData = MutableLiveData<List<Invoice>>()
    val error = MutableLiveData<String>()

    init {
        getInvoices()
    }

    fun getInvoices() {
        viewModelScope.launch {
            if (invoiceDao.getInvoiceCount() == 0) {
                getInvoicesFromApiAndSaveInRoom()
            } else {
                verifyChangesAndGetInvoices()
            }
        }
    }

    private fun getInvoicesFromApiAndSaveInRoom() {
        viewModelScope.launch {
            try {
                val invoicesApiResponse = api.getInvoices()
                val invoicesEntity = invoicesApiResponse.facturas.map {
                    InvoiceEntity(
                        date = it.fecha,
                        amount = it.importeOrdenacion,
                        status = it.descEstado
                    )
                }
                invoiceDao.deleteAllInvoices()
                invoiceDao.insertInvoices(invoicesEntity)
                getInvoicesFromRoom()

            } catch (e: IOException) {
                error.value = "Error de red: ${e.message}"
            } catch (e: HttpException) {
                error.value = "Error HTTP: ${e.code()} - ${e.message()}"
            } catch (e: Exception) {
                error.value = "Error desconocido: ${e.message}"
            }
        }
    }

    private fun verifyChangesAndGetInvoices() {
        viewModelScope.launch {
            if (api.getInvoices().numFacturas != invoiceDao.getInvoiceCount()) {
                getInvoicesFromApiAndSaveInRoom()
            } else {
                getInvoicesFromRoom()
            }
        }
    }

    private fun getInvoicesFromRoom() {
        viewModelScope.launch {
            val invoicesEntity = invoiceDao.getAllInvoices()
            invoicesLiveData.value = invoicesEntity.map {
                Invoice(
                    id = it.id,
                    fecha = it.date,
                    importeOrdenacion = it.amount,
                    descEstado = it.status
                )
            }
        }
    }
}