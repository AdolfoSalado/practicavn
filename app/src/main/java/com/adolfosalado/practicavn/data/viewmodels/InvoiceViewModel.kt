package com.adolfosalado.practicavn.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.models.InvoicesResponse
import com.adolfosalado.practicavn.data.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InvoiceViewModel() : ViewModel() {
    val invoicesResponse = MutableLiveData<InvoicesResponse>()
    val error = MutableLiveData<String>()

    fun getInvoices() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getInvoices()
                invoicesResponse.value = response
            } catch (e: Exception) {
                error.value = "Error: ${e.message}"
            }
        }
    }

}