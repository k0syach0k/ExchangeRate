package com.k0syach0k.exchange_rate.model

import android.os.Parcel
import android.os.Parcelable

data class Currency(
    val id: String,
    val numberCode: Int,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val previousValue: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(numberCode)
        parcel.writeString(charCode)
        parcel.writeInt(nominal)
        parcel.writeString(name)
        parcel.writeDouble(value)
        parcel.writeDouble(previousValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Currency> {
        override fun createFromParcel(parcel: Parcel): Currency {
            return Currency(parcel)
        }

        override fun newArray(size: Int): Array<Currency?> {
            return arrayOfNulls(size)
        }
    }
}
