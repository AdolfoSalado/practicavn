package com.adolfosalado.practicavn.data.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.database.InvoiceDatabaseClient
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import kotlinx.coroutines.launch

class InvoiceFilterViewModel(application: Application) : AndroidViewModel(application) {
    private val database = InvoiceDatabaseClient.getDatabase(application)
    private val invoiceDao = database.invoiceDao()

    private val _amountRange = MutableLiveData<Double>()
    val amountRange: LiveData<Double> = _amountRange

    private val _amountSelected = MutableLiveData<Double>()
    val amountSelected: LiveData<Double> = _amountSelected

    private val _statusList = MutableLiveData<List<String>>()
    val statusList: LiveData<List<String>> = _statusList

    private val _filter = MutableLiveData<InvoiceFilter>()
    val filter: LiveData<InvoiceFilter> = _filter

    init {
        // Inicializa con valores predeterminados, por ejemplo, una lista vacía
        _filter.value = InvoiceFilter(
            dateFrom = null,
            dateTo = null,
            amount = null,
            statusList = emptyList()
        )
        loadAmountRange()
        loadStatusList()
    }

    fun loadAmountRange() {
        viewModelScope.launch {
            val max = invoiceDao.getImporteMax()
            _amountRange.postValue(max)
        }
    }

    fun setAmountSelected(amount: Double) {
        _amountSelected.value = amount
        updateFilterAmount()
    }

    fun loadStatusList() {
        viewModelScope.launch {
            val statuses = invoiceDao.getDistinctStatuses()
            _statusList.postValue(statuses)
        }
    }

    private fun updateFilterAmount() {
        _filter.value = _filter.value?.copy(amount = _amountSelected.value?.toDouble())
        Log.d("FILTER_VIEWMODEL", "Filtro actualizado (importe): ${_filter.value}")
    }

    fun updateFilterDateFrom(newDate: Long?) {
        _filter.value = _filter.value?.copy(dateFrom = newDate)
        Log.d("FILTER_VIEWMODEL", "Filtro actualizado (fecha desde): ${_filter.value}")
    }

    fun updateFilterDateTo(newDate: Long?) {
        _filter.value = _filter.value?.copy(dateTo = newDate)
        Log.d("FILTER_VIEWMODEL", "Filtro actualizado (fecha hasta): ${_filter.value}")
    }

    fun updateFilterStatusList(statusList: List<String>) {
        _filter.value = _filter.value?.copy(statusList = statusList)
        Log.d("FILTER_VIEWMODEL", "Filtro actualizado (estados): ${_filter.value}")
    }
}
