package com.adolfosalado.practicavn.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.database.InvoiceDatabaseClient
import com.adolfosalado.practicavn.data.database.entities.InvoiceEntity
import kotlinx.coroutines.launch

class InvoiceFilterViewModel(application: Application) : AndroidViewModel(application) {
    private val database = InvoiceDatabaseClient.getDatabase(application)
    private val invoiceDao = database.invoiceDao()

    private val _amountRange = MutableLiveData<Float>()
    val amountRange: LiveData<Float> = _amountRange

    private val _amountSelected = MutableLiveData<Float>()
    val amountSelected: LiveData<Float> = _amountSelected

    private val _statusList = MutableLiveData<List<String>>()
    val statusList: LiveData<List<String>> = _statusList

    // Initialize the range and selected value
    init {
        loadAmountRange()
        loadStatusList()
    }

    fun loadAmountRange() {
        viewModelScope.launch {
            val max = invoiceDao.getImporteMax()

            _amountRange.postValue(max)

        }
    }

    fun setAmountSelected(amount: Float) {
        _amountSelected.value = amount
    }

    fun loadStatusList() {
        viewModelScope.launch {
            val statuses = invoiceDao.getDistinctStatuses()
            _statusList.postValue(statuses)
        }
    }


}