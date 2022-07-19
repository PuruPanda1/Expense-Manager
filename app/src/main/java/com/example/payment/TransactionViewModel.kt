package com.example.payment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.payment.db.Transaction
import com.example.payment.db.TransactionDatabase
import com.example.payment.db.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    var readAllTransaction: LiveData<List<Transaction>>
    var readAllIncomeTransaction: LiveData<List<Transaction>>
    var readAllExpenseTransaction: LiveData<List<Transaction>>
    private val repository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getInstance(application).transacitonDao()
        repository = TransactionRepository(transactionDao)
        readAllTransaction = repository.readAllData
        readAllIncomeTransaction = repository.incomeData
        readAllExpenseTransaction = repository.expenseData
    }

    fun insideViewModel() {
        Log.d("ViewModel", "insideViewModel: hello")
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