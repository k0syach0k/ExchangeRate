package com.k0syach0k.exchange_rate.ui.list

import android.content.Context
import androidx.room.Room
import com.k0syach0k.exchange_rate.db.DataBase
import com.k0syach0k.exchange_rate.model.JSONmodel
import com.k0syach0k.exchange_rate.model.currency.Currency
import com.k0syach0k.exchange_rate.model.date_time.DateTime
import com.k0syach0k.exchange_rate.network.Network
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ListRepository(context: Context) {
    private val db = Room
        .databaseBuilder(context, DataBase::class.java, "database")
        .build()

    private val currencyDao = db.currencyDao()
    private val dateTimeDao = db.dateTimeDao()

    suspend fun getRateFromNetwork() {
        val jsonObject = getJSONObjectFromServer()
        val currencyList = getCurrencyListFromJSON(jsonObject)
        val date = getDateFromJSON(jsonObject)
        if (currencyList.isNotEmpty() && date.isNotEmpty()) {
            val savedDateTime = dateTimeDao.getDateTimeString()
            when {
                date != savedDateTime -> {
                    saveDate(date)
                    saveCurrency(currencyList)
                }
                date == savedDateTime -> throw Exception("Exchange rate is still valid")
            }
        }
    }

    private fun getCurrencyListFromJSON(jsonObject: JSONObject): List<Currency> {
        val valuteObject = jsonObject.getJSONObject(JSONmodel.VALUTE_ID)
        val keys = valuteObject.keys()
        val listCurrency = mutableListOf<Currency>()
        while (keys.hasNext()) {
            val currentKey = keys.next()
            val currentJSON = valuteObject.getJSONObject(currentKey)
            val currency = Currency(
                currentJSON.getString(JSONmodel.CURRENCY_ID),
                currentJSON.getString(JSONmodel.CURRENCY_SYMBOL_CODE),
                currentJSON.getInt(JSONmodel.CURRENCY_NOMINAL),
                currentJSON.getString(JSONmodel.CURRENCY_NAME),
                currentJSON.getDouble(JSONmodel.CURRENCY_VALUE),
                currentJSON.getDouble(JSONmodel.CURRENCY_PREVIOUS_VALUE)
            )
            listCurrency.add(currency)
        }
        return listCurrency
    }

    private fun getDateFromJSON(jsonObject: JSONObject): String {
        val offsetDateTime =
            OffsetDateTime.parse(jsonObject.getString(JSONmodel.DATE_ID))
        val formatter =
            DateTimeFormatter.ofPattern(JSONmodel.DATE_FORMATTER, Locale(JSONmodel.DATE_LOCALE))
        val dateTimeString = offsetDateTime.format(formatter)
        return dateTimeString.orEmpty()
    }

    private suspend fun getJSONObjectFromServer(): JSONObject {
        return suspendCancellableCoroutine {
            val response = Network.getCall().execute()
            when (response.isSuccessful) {
                true -> it.resume(JSONObject(response.body?.string().orEmpty()))
                false -> it.resumeWithException(Exception("Request unsuccessfully"))
            }
        }
    }

    private fun saveDate(dateTimeString: String) {
        dateTimeDao.saveDateTime(DateTime(1, dateTimeString))
    }

    private fun saveCurrency(currencyList: List<Currency>) {
        currencyDao.deleteAll()
        currencyDao.insertAll(currencyList)
    }

    fun loadCurrency() = currencyDao.getAll()

    fun loadDateTime() = dateTimeDao.getDateTimeFlow()
}
