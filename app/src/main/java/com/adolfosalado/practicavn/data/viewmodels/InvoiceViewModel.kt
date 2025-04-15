package com.adolfosalado.practicavn.data.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.database.InvoiceDatabaseClient
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class InvoiceViewModel(application: Application) : AndroidViewModel(application) {
    private val database = InvoiceDatabaseClient.getDatabase(application)
    private val invoiceDao = database.invoiceDao()
    private val api = RetrofitClient.api

    private val _invoicesLiveData = MutableLiveData<List<Invoice>>()
    val invoicesLiveData: LiveData<List<Invoice>> get() = _invoicesLiveData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val filterLiveData = MutableLiveData<InvoiceFilter>()

    init {
        checkForApiChangesAndUpdateRoom()
    }

    private fun checkForApiChangesAndUpdateRoom() {
        viewModelScope.launch {
            try {
                val apiResponse = api.getInvoices()
                val apiNumberInvoices = apiResponse.numFacturas
                val roomNumberInvoices = invoiceDao.getInvoiceCount()

                if (apiNumberInvoices != roomNumberInvoices) {
                    invoiceDao.deleteAllInvoices()
                    invoiceDao.insertInvoices(apiResponse.facturas.map { mapInvoiceToEntity(it) })
                }

                val updatedInvoices = invoiceDao.getAllInvoices().map { mapEntityToInvoice(it) }
                _invoicesLiveData.postValue(updatedInvoices)

            } catch (e: Exception) {
                _error.postValue("Error al verificar cambios: ${e.message}")
                Log.e("API_ERROR", "Error al consultar la API: ${e.message}")
            }
        }
    }

    fun applyFilter(filter: InvoiceFilter) {
        viewModelScope.launch {
            try {
                if (filter.dateFrom != null && filter.dateTo != null && filter.dateFrom > filter.dateTo) {
                    _error.postValue("La fecha 'Desde' no puede ser mayor que la fecha 'Hasta'")
                    return@launch
                }

                val filteredInvoices = invoiceDao.getFilteredInvoices(
                    dateFrom = filter.dateFrom,
                    dateTo = filter.dateTo,
                    amount = filter.amount,
                    statusList = filter.statusList
                )
                _invoicesLiveData.postValue(filteredInvoices.map { mapEntityToInvoice(it) })
            } catch (e: Exception) {
                _error.postValue("Error al aplicar filtro: ${e.message}")
            }
        }
    }

    private fun mapInvoiceToEntity(invoice: Invoice): InvoiceEntity {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimestamp = dateFormat.parse(invoice.date)?.time ?: 0L
        return InvoiceEntity(0, dateTimestamp, invoice.amount, invoice.status)
    }

    private fun mapEntityToInvoice(entity: InvoiceEntity): Invoice {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return Invoice(dateFormat.format(entity.date), entity.amount, entity.status)
    }
}
