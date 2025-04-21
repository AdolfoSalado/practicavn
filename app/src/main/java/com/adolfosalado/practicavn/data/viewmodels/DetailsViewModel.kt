package com.adolfosalado.practicavn.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adolfosalado.practicavn.data.models.SmartSolarDetails
import com.adolfosalado.practicavn.data.usecases.GetSmartSolarDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val getSmartSolarDetailsUseCase: GetSmartSolarDetailsUseCase) :
    ViewModel() {

    private val _details = MutableLiveData<SmartSolarDetails?>()
    val details: LiveData<SmartSolarDetails> = _details as LiveData<SmartSolarDetails>

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetails() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getSmartSolarDetailsUseCase()
            if (result != null) {
                _details.postValue(result)
            } else {
                _error.postValue("Error: No se pudieron cargar los detalles")
            }
            _isLoading.value = false
        }
    }
}