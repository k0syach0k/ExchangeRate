package com.k0syach0k.exchange_rate.model.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    @PrimaryKey
    val numberCode: Int,
    @ColumnInfo(name = "char_code")
    val charCode: String,
    @ColumnInfo(name = "nominal")
    val nominal: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "value")
    val value: Double,
    @ColumnInfo(name = "previous_value")
    val previousValue: Double
)
