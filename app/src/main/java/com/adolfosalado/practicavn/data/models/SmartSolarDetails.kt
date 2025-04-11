package com.adolfosalado.practicavn.data.models

import com.google.gson.annotations.SerializedName

data class SmartSolarDetails(
    @SerializedName("cau") var cau: String? = null,
    @SerializedName("estadoAlta") var estadoAlta: String? = null,
    @SerializedName("tipoAutoconsumo") var tipoAutoconsumo: String? = null,
    @SerializedName("compensacion") var compensacion: String? = null,
    @SerializedName("potencia") var potencia: String? = null

)