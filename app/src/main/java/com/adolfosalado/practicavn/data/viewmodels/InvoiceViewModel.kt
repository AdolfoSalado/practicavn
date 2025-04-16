package com.adolfosalado.practicavn.data.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
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

    private val _invoicesLiveData = MutableLiveData<List<Invoice>>().apply { value = emptyList() }
    val invoicesLiveData: LiveData<List<Invoice>> get() = _invoicesLiveData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _filterLiveData = MutableLiveData<InvoiceFilter>()
    val filterLiveData: MutableLiveData<InvoiceFilter> get() = _filterLiveData

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
                val filteredInvoices = invoiceDao.getFilteredInvoices(
                    dateFrom = filter.dateFrom,
                    dateTo = filter.dateTo,
                    amount = filter.amount,
                    statusList = filter.statusList ?: emptyList(),
                    statusListSize = filter.statusList?.size ?: 0
                )

                val mappedInvoices = filteredInvoices.map { mapEntityToInvoice(it) }
                _invoicesLiveData.postValue(mappedInvoices)
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
        return Invoice(entity.status, entity.amount, dateFormat.format(entity.date))
    }
}
