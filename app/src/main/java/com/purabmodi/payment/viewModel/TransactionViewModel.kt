package com.purabmodi.payment.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.purabmodi.payment.modals.CustomTimeData
import com.purabmodi.payment.modals.customData
import com.purabmodi.payment.transactionDb.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    var readAllTransaction: LiveData<List<Transaction>>
    var readAllIncomeTransaction: LiveData<List<Transaction>>
    var readAllExpenseTransaction: LiveData<List<Transaction>>
    var readSumByCategory: LiveData<List<MyTypes>>
    var readDifferenceSum: LiveData<Float>
    var readMonthlySpends: LiveData<Float>
    var incomeSum: LiveData<Float>
    var expenseSum: LiveData<Float>
    var readAccountDetails: LiveData<List<AccountDetails>>
    private val repository: TransactionRepository

    init {
        val transactionDao = TransactionDatabase.getInstance(application).transacitonDao()
        repository = TransactionRepository(transactionDao)
        readAllTransaction = repository.readAllTransactions
        readAllIncomeTransaction = repository.readIncomeTransactions
        readAllExpenseTransaction = repository.readExpenseTransactions
        readDifferenceSum = repository.differenceSum
        readMonthlySpends = repository.monthlySpends
        incomeSum = repository.incomeSum
        expenseSum = repository.expenseSum
        readSumByCategory = repository.readSumByCategory
        readAccountDetails = repository.readAccountDetails
    }

    val dates: MutableLiveData<List<Long>> = MutableLiveData()

    val readCategoriesByDuration: LiveData<List<MyTypes>> = Transformations.switchMap(dates) {
        repository.readCategoriesByDuration(it[0], it[1])
    }
    val readExpenseSumByDuration: LiveData<Float> = Transformations.switchMap(dates) {
        repository.readExpenseSumByDuration(it[0], it[1])
    }

    var transactionTypeDetails: MutableLiveData<TransactionTypeData> = MutableLiveData()

    val readTransactionsByCategory: LiveData<List<Transaction>> =
        Transformations.switchMap(transactionTypeDetails) {
            if (it.startDate == 0L) {
                repository.getTransactionsByCategory(it.transactionType)
            } else {
                repository.getRangeTransactionsData(it.transactionType, it.startDate, it.endDate)
            }
        }

    val readSingleTransactionType: LiveData<MyTypes> =
        Transformations.switchMap(transactionTypeDetails) {
            if (it.startDate == 0L) {
                repository.getMonthlySingleTransactionType(
                    it.transactionType
                )
            } else {
                repository.getSingleTransactionType(it.transactionType, it.startDate, it.endDate)
            }
        }

    fun setTransactionTypeData(details: TransactionTypeData) {
        transactionTypeDetails.value = details
    }

    fun setCustomDurationData(dates: List<Long>) {
        this.dates.value = dates
    }

    private var customTimeData = MutableLiveData<CustomTimeData>()

    val readTransactionsByDuration: LiveData<List<Transaction>> =
        Transformations.switchMap(customTimeData) {
            repository.readTransactionsByDuration(
                it.categoryList,
                it.accountList,
                it.startDate,
                it.endDate
            )
        }

    fun setCustomTimeData(data: CustomTimeData) {
        this.customTimeData.value = data
    }

    private var customData = MutableLiveData<customData>()

    val readTransactionsByMonth: LiveData<List<Transaction>> =
        Transformations.switchMap(customData) {
            repository.readTransactionsByMonth(it.categoryList, it.accountList, it.monthList)
        }

    fun setCustomData(data: customData) {
        this.customData.value = data
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

    fun deleteAccountTransaction(accountName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAccountTransactions(accountName)
        }
    }
}

data class TransactionTypeData(
    val transactionType: String,
    val startDate: Long,
    val endDate: Long
)