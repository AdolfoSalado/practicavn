package com.adolfosalado.practicavn.data.models

import android.os.Parcel
import android.os.Parcelable

data class InvoiceFilter(
    val dateFrom: Long? = null,
    val dateTo: Long? = null,
    val amount: Double? = null,
    val statusList: List<String>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(dateFrom)
        parcel.writeValue(dateTo)
        parcel.writeValue(amount)
        parcel.writeStringList(statusList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InvoiceFilter> {
        override fun createFromParcel(parcel: Parcel): InvoiceFilter {
            return InvoiceFilter(parcel)
        }

        override fun newArray(size: Int): Array<InvoiceFilter?> {
            return arrayOfNulls(size)
        }
    }
}