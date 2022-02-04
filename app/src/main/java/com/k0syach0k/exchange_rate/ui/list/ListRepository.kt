package com.k0syach0k.exchange_rate.ui.list

import android.content.Context
import androidx.room.Room
import com.k0syach0k.exchange_rate.db.DataBase
import com.k0syach0k.exchange_rate.model.JSONmodel
import com.k0syach0k.exchange_rate.model.currency.Currency
import com.k0syach0k.exchange_rate.model.date_time.DateTime
import com.k0syach0k.exchange_rate.network.Network
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ListRepository(context: Context) {
    private val db = Room
        .databaseBuilder(context, DataBase::class.java, "database")
        .build()

    private val currencyDao = db.currencyDao()
    private val dateTimeDao = db.dateTimeDao()

    fun getRateFromNetwork(onComplete: (List<Currency>, String) -> Unit) {
        Network.getCall().apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val jsonObject = JSONObject(response.body?.string().orEmpty())

                        val offsetDateTime =
                            OffsetDateTime.parse(jsonObject.getString(JSONmodel.DATE_ID))
                        val formatter =
                            DateTimeFormatter.ofPattern(JSONmodel.DATE_FORMATTER, Locale(JSONmodel.DATE_LOCALE))
                        val dateTimeString = offsetDateTime.format(formatter)

                        val valuteObject = jsonObject.getJSONObject(JSONmodel.VALUTE_ID)
                        val keys = valuteObject.keys()
                        val listCurrency = mutableListOf<Currency>()
                        while (keys.hasNext()) {
                            val currentKey = keys.next()
                            val currentJSON = valuteObject.getJSONObject(currentKey)
                            val currency = Currency(
                                currentJSON.getInt(JSONmodel.CURRENCY_NUMBER_CODE),
                                currentJSON.getString(JSONmodel.CURRENCY_SYMBOL_CODE),
                                currentJSON.getInt(JSONmodel.CURRENCY_NOMINAL),
                                currentJSON.getString(JSONmodel.CURRENCY_NAME),
                                currentJSON.getDouble(JSONmodel.CURRENCY_VALUE),
                                currentJSON.getDouble(JSONmodel.CURRENCY_PREVIOUS_VALUE)
                            )
                            listCurrency.add(currency)
                        }
                        if (listCurrency.isNotEmpty()) {
                            onComplete(listCurrency, dateTimeString)
                            saveData(dateTimeString)
                            saveCurrency(listCurrency)
                        }
                    } else {
                        throw Exception("Request unsuccessfully")
                    }
                }
            })
        }
    }

    private fun saveData(dateTimeString: String) {
        dateTimeDao.insertDateTime(DateTime(1, dateTimeString))
    }

    private fun saveCurrency(currencyList: List<Currency>) {
        currencyDao.deleteAll()
        currencyDao.insertAll(currencyList)
    }

    fun loadCurrency(): List<Currency> {
        return currencyDao.getAll()
    }

    fun loadDateTime(): String {
        return dateTimeDao.getDateTime()
    }
}
