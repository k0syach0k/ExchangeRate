package com.k0syach0k.exchange_rate.model.date_time

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DateTimeDao {
    @Query("SELECT \"date_time\" FROM datetime LIMIT 1")
    fun getDateTime(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDateTime(dateTime: DateTime)
}
