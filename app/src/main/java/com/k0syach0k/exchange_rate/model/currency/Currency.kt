package com.k0syach0k.exchange_rate.model.currency

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val ID: String,
    @ColumnInfo(name = "charCode")
    val charCode: String,
    @ColumnInfo(name = "nominal")
    val nominal: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "value")
    val value: Double,
    @ColumnInfo(name = "previous_value")
    val previousValue: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ID)
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
