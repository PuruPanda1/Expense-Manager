package com.example.payment.cryptoDb

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CryptoViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var cryptoData: LiveData<List<CryptoTransaction>>
    private val repository: CryptoDbRepository

    init {
        val dao = CryptoTransactionDatabase.getInstance(application).cryptoDao()
        repository = CryptoDbRepository(dao)
    }


    fun getCryptoData() {
        cryptoData = repository.readAllData
    }

    fun insertCryptoTransaction(cryptoTransaction: CryptoTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCryptoTransaction(cryptoTransaction)
        }
    }

    fun updateCryptoTransaction(cryptoTransaction: CryptoTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCryptoTransaction(cryptoTransaction)
        }
    }
}