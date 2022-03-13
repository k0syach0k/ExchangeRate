package com.k0syach0k.exchange_rate.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.k0syach0k.exchange_rate.model.currency.Currency
import com.k0syach0k.exchange_rate.utils.SingleLiveEvent
import kotlinx.coroutines.*

class ListViewModel(application: Application) : AndroidViewModel(application) {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(30000)
                getCurrencyRateFromNetwork()
            }
        }
    }

    private val repository = ListRepository(application.baseContext)

    private val listCurrencyLiveData: MutableLiveData<List<Currency>> =
        MutableLiveData<List<Currency>>().also {
            getCurrencyFromDB()
        }

    private val dataRateLiveData: MutableLiveData<String> =
        MutableLiveData<String>().also {
            getDataFromDB()
        }

    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val messageLiveData = SingleLiveEvent<String>()

    val listCurrency: LiveData<List<Currency>>
        get() = listCurrencyLiveData

    val dataRate: LiveData<String>
        get() = dataRateLiveData

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    val message: LiveData<String>
        get() = messageLiveData as LiveData<String>

    fun getCurrencyRate() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingLiveData.postValue(true)
            getCurrencyRateFromNetwork()
            isLoadingLiveData.postValue(false)
        }
    }

    private fun getCurrencyFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listCurrencyLiveData.postValue(repository.loadCurrency())
            } catch (t: Throwable) {
                messageLiveData.postValue(t.message)
            }
        }
    }

    private fun getDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRateLiveData.postValue(repository.loadDateTime())
            } catch (t: Throwable) {
                messageLiveData.postValue(t.message)
            }
        }
    }

    private fun getCurrencyRateFromNetwork() {
        try {
            repository.getRateFromNetwork { listCurrency, offsetDateTime ->
                listCurrencyLiveData.postValue(listCurrency)
                dataRateLiveData.postValue(offsetDateTime)
                messageLiveData.postValue("Курс валют обновлён")
            }
        } catch (t: Throwable) {
            messageLiveData.postValue(t.message)
        }
    }
}
