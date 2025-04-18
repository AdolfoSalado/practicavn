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

    private val _dateFrom = MutableLiveData<Long?>()
    val dateFrom: LiveData<Long?> = _dateFrom

    private val _dateTo = MutableLiveData<Long?>()
    val dateTo: LiveData<Long?> = _dateTo

    private val _amount = MutableLiveData<Double>()
    val amount: LiveData<Double> = _amount

    private val _maxAmount = MutableLiveData<Double>()
    val maxAmount: LiveData<Double> = _maxAmount

    private val _statusList = MutableLiveData<List<String>>()
    val statusList: LiveData<List<String>> = _statusList

    private val _statusesSelected = MutableLiveData<List<String>>()
    val statusesSelected: LiveData<List<String>> = _statusesSelected

    private val _filter = MutableLiveData<InvoiceFilter>()
    val filter: LiveData<InvoiceFilter> = _filter

    init {
        _filter.value = InvoiceFilter(null, null, null, emptyList())
        loadMaxAmount()
        loadStatusList()
    }

    private fun loadMaxAmount() {
        viewModelScope.launch {
            _maxAmount.postValue(invoiceDao.getImporteMax() ?: 100.0)
        }
    }

    private fun loadStatusList() {
        viewModelScope.launch {
            _statusList.postValue(invoiceDao.getDistinctStatuses() ?: emptyList())
        }
    }

    fun setDateFrom(date: Long?) {
        _dateFrom.value = date
        updateFilter { it.copy(dateFrom = date) }
    }

    fun setDateTo(date: Long?) {
        _dateTo.value = date
        updateFilter { it.copy(dateTo = date) }
    }

    fun setAmountSelected(amount: Double) {
        _amount.value = amount
        updateFilter { it.copy(amount = if (amount > 0) amount else null) }
    }

    fun setStatusesSelected(statuses: List<String>) {
        _statusesSelected.value = statuses
        updateFilter { it.copy(statusList = statuses) }
    }

    fun setFilter(filter: InvoiceFilter) {
        _filter.value = filter
        _dateFrom.value = filter.dateFrom
        _dateTo.value = filter.dateTo
        _amount.value = filter.amount ?: 0.0
        _statusesSelected.value = filter.statusList ?: emptyList()
    }

    private fun updateFilter(update: (InvoiceFilter) -> InvoiceFilter) {
        val current = _filter.value ?: InvoiceFilter(null, null, null, emptyList())
        _filter.value = update(current)
        Log.d("FILTER_VIEWMODEL", "Filtro actualizado: ${_filter.value}")
    }

}
