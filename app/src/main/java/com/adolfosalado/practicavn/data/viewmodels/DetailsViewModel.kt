package com.adolfosalado.practicavn.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.models.SmartSolarDetails
import com.adolfosalado.practicavn.data.network.RetrofitClient
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val detailsApi = RetrofitClient.apiDetails
    private val _details = MutableLiveData<SmartSolarDetails>()
    val details: LiveData<SmartSolarDetails> = _details

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getDetails() {
        viewModelScope.launch {
            try {
                val result = detailsApi.getSmartSolarDetails().body().let {
                    SmartSolarDetails(
                        it?.cau,
                        it?.estadoAlta,
                        it?.tipoAutoconsumo,
                        it?.compensacion,
                        it?.potencia
                    )
                }
                _details.postValue(result)
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")

            }
        }
    }

}