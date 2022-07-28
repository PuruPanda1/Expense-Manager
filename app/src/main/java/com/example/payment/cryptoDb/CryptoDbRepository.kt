package com.example.payment.cryptoDb

import androidx.lifecycle.LiveData

class CryptoDbRepository(private val cryptoDao: CryptoDao) {
    val readAllData: LiveData<List<CryptoTransaction>> = cryptoDao.getAllStudents()
    val totalAmount: LiveData<Double> = cryptoDao.getTotalAmount()
    val profitLoss: LiveData<Double> = cryptoDao.getProfitLoss()

    suspend fun insertCryptoTransaction(cryptoTransaction: CryptoTransaction){
        cryptoDao.insertCryptoTransaction(cryptoTransaction)
    }

    suspend fun updateCryptoTransaction(cryptoTransaction: CryptoTransaction){
        cryptoDao.updateCryptoTransaction(cryptoTransaction)
    }

    suspend fun deleteCryptoTransaction(cryptoTransaction: CryptoTransaction){
        cryptoDao.deleteCryptoTransaction(cryptoTransaction)
    }
}