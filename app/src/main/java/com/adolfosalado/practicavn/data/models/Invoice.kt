package com.adolfosalado.practicavn.data.models

import com.google.gson.annotations.SerializedName

data class InvoicesResponse(
    @SerializedName("numFacturas") val numFacturas: Int,
    @SerializedName("facturas") val facturas: List<Invoice>
)

data class Invoice(
    @SerializedName("descEstado") val descEstado: String,
    @SerializedName("importeOrdenacion") val importeOrdenacion: Double,
    @SerializedName("fecha") val fecha: String
)