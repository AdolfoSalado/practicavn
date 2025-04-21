package com.adolfosalado.practicavn.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.usecases.GetFilteredInvoicesUseCase
import com.adolfosalado.practicavn.data.usecases.GetInvoicesUseCase
import com.adolfosalado.practicavn.data.usecases.SyncInvoicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val getFilteredInvoicesUseCase: GetFilteredInvoicesUseCase,
    private val syncInvoicesUseCase: SyncInvoicesUseCase
) : ViewModel() {

    private val _invoicesLiveData = MutableLiveData<List<Invoice>>().apply { value = emptyList() }
    val invoicesLiveData: LiveData<List<Invoice>> get() = _invoicesLiveData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _filterLiveData = MutableLiveData<InvoiceFilter>()
    val filterLiveData: LiveData<InvoiceFilter> get() = _filterLiveData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getInvoices()
    }

    private fun getInvoices() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                syncInvoicesUseCase()
                val updatedInvoices = getInvoicesUseCase()

                // Aplica el ordenamiento antes de actualizar el LiveData
                val sortedInvoices = sortInvoices(updatedInvoices)

                _invoicesLiveData.postValue(sortedInvoices)
                _isLoading.value = false

            } catch (e: Exception) {
                _error.postValue("Error al obtener las facturas: ${e.message}")
                Log.e("API_ERROR", "Error al consultar la API: ${e.message}")
                _isLoading.value = false

            }
        }
    }

    fun applyFilter(filter: InvoiceFilter) {
        _isLoading.value = true // Indica que la carga del filtro ha comenzado
        viewModelScope.launch {
            try {
                val filteredInvoices = getFilteredInvoicesUseCase(filter)

                _invoicesLiveData.postValue(sortInvoices(filteredInvoices))
                _isLoading.value = false // Indica que la carga del filtro ha comenzado

            } catch (e: Exception) {
                _error.postValue("Error al aplicar filtro: ${e.message}")
                _isLoading.value = false // Indica que la carga del filtro ha comenzado
            }
        }
    }


    private fun sortInvoices(invoices: List<Invoice>): List<Invoice> {
        return invoices.sortedWith(
            compareByDescending<Invoice> { it.status == "Pendiente de pago" }
                .thenByDescending {
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).parse(it.date)?.time ?: 0L
                }
        )
    }

    fun setFilter(filter: InvoiceFilter) {
        _filterLiveData.value = filter
    }
}