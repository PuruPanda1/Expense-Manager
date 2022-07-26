package com.example.payment.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.payment.transactionDb.Transaction
import com.example.payment.transactionDb.TransactionDatabase
import com.example.payment.transactionDb.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    var readAllTransaction: LiveData<List<Transaction>>
    var readAllIncomeTransaction: LiveData<List<Transaction>>
    var readAllExpenseTransaction: LiveData<List<Transaction>>
    var readDifferenceSum : LiveData<Float>
    var readMonthlySpends : LiveData<Float>
    var incomeSum : LiveData<Float>
    var expenseSum : LiveData<Float>
    private val repository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getInstance(application).transacitonDao()
        repository = TransactionRepository(transactionDao)
        readAllTransaction = repository.readAllData
        readAllIncomeTransaction = repository.incomeData
        readAllExpenseTransaction = repository.expenseData
        readDifferenceSum = repository.differenceSum
        readMonthlySpends = repository.monthlySpends
        incomeSum = repository.incomeSum
        expenseSum = repository.expenseSum
    }

    fun insertTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTransaction(transaction)
        }
    }
}