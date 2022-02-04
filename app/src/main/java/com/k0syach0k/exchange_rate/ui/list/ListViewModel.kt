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
                try {
                    repository.getRateFromNetwork() { listCurrency, offsetDateTime ->
                        listCurrencyLiveData.postValue(listCurrency)
                        dataRateLiveData.postValue(offsetDateTime)
                    }
                    messageLiveData.postValue("Курс валют обновлён")
                } catch (t: Throwable) {
                    messageLiveData.postValue(t.message)
                }
            }
        }
    }

    private val repository = ListRepository(application.baseContext)

    private val listCurrencyLiveData: MutableLiveData<List<Currency>> by lazy {
        MutableLiveData<List<Currency>>().also {
            loadCurrency()
        }
    }

    private val dataRateLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            loadDataRate()
        }
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

    // private val timerJob =

    fun getCurrencyRateFromNetwork() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingLiveData.postValue(true)
            try {
                repository.getRateFromNetwork() { listCurrency, offsetDateTime ->
                    listCurrencyLiveData.postValue(listCurrency)
                    dataRateLiveData.postValue(offsetDateTime)
                }
                messageLiveData.postValue("Курс валют обновлён")
            } catch (t: Throwable) {
                messageLiveData.postValue(t.message)
            } finally {
                isLoadingLiveData.postValue(false)
            }
        }
    }

    private fun loadCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listCurrencyLiveData.postValue(repository.loadCurrency())
            } catch (t: Throwable) {
                messageLiveData.postValue(t.message)
            }
        }
    }

    private fun loadDataRate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRateLiveData.postValue(repository.loadDateTime())
            } catch (t: Throwable) {
                messageLiveData.postValue(t.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
