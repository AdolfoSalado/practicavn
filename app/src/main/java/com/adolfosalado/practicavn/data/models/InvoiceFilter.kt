package com.adolfosalado.practicavn.data.models

data class InvoiceFilter(
    val dateFrom: Long? = null,
    val dateTo: Long? = null,
    val amountSelected: Float? = null,
    val statusList: List<String>? = null
)
