package com.example.payment.cryptoDb

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CryptoViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var cryptoData: LiveData<List<CryptoTransaction>>
    lateinit var totalAmount:LiveData<Double>
    lateinit var profitLoss:LiveData<Double>
    private val repository: CryptoDbRepository

    init {
        val dao = CryptoTransactionDatabase.getInstance(application).cryptoDao()
        repository = CryptoDbRepository(dao)
    }

    fun getTotalAmount(){
        totalAmount = repository.totalAmount
    }

    fun getCryptoData() {
        cryptoData = repository.readAllData
        totalAmount = repository.totalAmount
        profitLoss = repository.profitLoss
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

    fun deleteCryptoTransaction(cryptoTransaction: CryptoTransaction){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCryptoTransaction(cryptoTransaction)
        }
    }
}