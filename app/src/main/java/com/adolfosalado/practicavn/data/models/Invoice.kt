package com.adolfosalado.practicavn.data.models

import com.google.gson.annotations.SerializedName

data class InvoicesResponse(
    @SerializedName("numFacturas") val numFacturas: Int,
    @SerializedName("facturas") val facturas: List<Invoice>
)

data class Invoice(
    @SerializedName("descEstado") val status: String,
    @SerializedName("importeOrdenacion") val amount: Double,
    @SerializedName("fecha") val date: String // Mantén como String para convertir después
)