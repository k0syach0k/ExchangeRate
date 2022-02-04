package com.k0syach0k.exchange_rate.model.date_time

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DateTime(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "date_time")
    val dateTimeString: String
)
