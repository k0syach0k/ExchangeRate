package com.k0syach0k.exchange_rate.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.k0syach0k.exchange_rate.model.currency.Currency
import com.k0syach0k.exchange_rate.model.currency.CurrencyDao
import com.k0syach0k.exchange_rate.model.date_time.DateTime
import com.k0syach0k.exchange_rate.model.date_time.DateTimeDao

@Database(entities = [Currency::class, DateTime::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun dateTimeDao(): DateTimeDao
}
