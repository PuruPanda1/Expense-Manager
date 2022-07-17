package com.example.payment.db

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val readAllData: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val incomeData: LiveData<List<Transaction>> = transactionDao.getIncomeTransactions()
    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

}