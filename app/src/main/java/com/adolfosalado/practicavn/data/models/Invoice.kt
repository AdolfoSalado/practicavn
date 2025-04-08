package com.adolfosalado.practicavn.data.models

import com.google.gson.annotations.SerializedName

data class Invoice(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("status") val status: String
)