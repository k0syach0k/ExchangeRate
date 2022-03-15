package com.k0syach0k.exchange_rate.model.currency

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency")
    fun getAll(): Flow<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(currencyList: List<Currency>)

    @Query("DELETE FROM currency")
    fun deleteAll()
}
