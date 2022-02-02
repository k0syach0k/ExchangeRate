package com.k0syach0k.exchange_rate.ui.list

import com.k0syach0k.exchange_rate.model.Currency
import com.k0syach0k.exchange_rate.network.Network
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.OffsetDateTime

class ListRepository {
    fun getRateFromNetwork(onComplete: (List<Currency>, LocalDateTime) -> Unit) {
        Network.getCall().apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    throw e
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            val jsonObject = JSONObject(response.body?.string().orEmpty())

                            val localDate = OffsetDateTime.parse(jsonObject.getString("Date")).toLocalDateTime()

                            val valuteObject = jsonObject.getJSONObject("Valute")
                            val keys = valuteObject.keys()
                            val listCurrency = mutableListOf<Currency>()
                            while (keys.hasNext()) {
                                val currentKey = keys.next()
                                val currentJSON = valuteObject.getJSONObject(currentKey)
                                val currency = Currency(
                                    currentJSON.getString("ID"),
                                    currentJSON.getInt("NumCode"),
                                    currentJSON.getString("CharCode"),
                                    currentJSON.getInt("Nominal"),
                                    currentJSON.getString("Name"),
                                    currentJSON.getDouble("Value"),
                                    currentJSON.getDouble("Previous")
                                )
                                listCurrency.add(currency)
                            }
                            if (listCurrency.isNotEmpty()) {
                                onComplete(listCurrency, localDate)
                            }
                        } catch (e: Exception) {
                            throw e
                        }
                    } else {
                        throw Exception("Request unsuccessfully")
                    }
                }
            })
        }
    }
}
