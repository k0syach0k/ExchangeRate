package com.k0syach0k.exchange_rate.ui.list

import android.app.Application
import androidx.lifecycle.*
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

    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val messageLiveData = SingleLiveEvent<String>()

    val listCurrency = repository.loadCurrency().asLiveData()
    val dateRate = repository.loadDateTime().asLiveData()

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

    private suspend fun getCurrencyRateFromNetwork() {
        try {
            repository.getRateFromNetwork()
            messageLiveData.postValue("Курс валют обновлён")
        } catch (t: Throwable) {
            messageLiveData.postValue(t.message)
        }
    }
}
