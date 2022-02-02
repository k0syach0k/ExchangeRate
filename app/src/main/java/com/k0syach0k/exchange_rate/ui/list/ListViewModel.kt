package com.k0syach0k.exchange_rate.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.k0syach0k.exchange_rate.model.Currency
import com.k0syach0k.exchange_rate.utils.SingleLiveEvent
import kotlinx.coroutines.*
import java.time.LocalDateTime

class ListViewModel() : ViewModel() {

    private val repository = ListRepository()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val listCurrencyLiveData = MutableLiveData<List<Currency>>()
    private val dataRateLiveData = MutableLiveData<LocalDateTime>()
    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val messageLiveData = SingleLiveEvent<String>()

    val listCurrency: LiveData<List<Currency>>
        get() = listCurrencyLiveData

    val dataRate: LiveData<LocalDateTime>
        get() = dataRateLiveData

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    val message: LiveData<String>
        get() = messageLiveData as LiveData<String>

    fun getCurrencyRateFromNetwork() {
        scope.launch {
            isLoadingLiveData.postValue(true)
            try {
                repository.getRateFromNetwork() { listCurrency, localDateTime ->
                    listCurrencyLiveData.postValue(listCurrency)
                    dataRateLiveData.postValue(localDateTime)
                }
                messageLiveData.postValue("Курс валют обновлён")
            } catch (t: Throwable) {
                messageLiveData.postValue(t.message)
            } finally {
                isLoadingLiveData.postValue(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
