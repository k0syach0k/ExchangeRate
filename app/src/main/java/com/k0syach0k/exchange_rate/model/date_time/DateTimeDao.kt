package com.k0syach0k.exchange_rate.model.date_time

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DateTimeDao {
    @Query("SELECT \"date_time\" FROM datetime LIMIT 1")
    fun getDateTimeFlow(): Flow<String>

    @Query("SELECT \"date_time\" FROM datetime LIMIT 1")
    fun getDateTimeString(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDateTime(dateTime: DateTime)
}
