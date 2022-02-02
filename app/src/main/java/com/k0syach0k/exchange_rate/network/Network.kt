package com.k0syach0k.exchange_rate.network

import okhttp3.*

object Network {
    private val client = OkHttpClient.Builder()
        .build()

    fun getCall(): Call {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("www.cbr-xml-daily.ru")
            .addPathSegment("daily_json.js")
            .build()

        val request = Request.Builder()
            .get()
            .url(url)
            .build()

        return client.newCall(request)
    }
}
