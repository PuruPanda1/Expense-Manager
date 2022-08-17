package com.example.payment.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.payment.modals.customData
import com.example.payment.transactionDb.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    var readAllTransaction: LiveData<List<Transaction>>
    var readAllIncomeTransaction: LiveData<List<Transaction>>
    var readAllExpenseTransaction: LiveData<List<Transaction>>
    var readTransactionTypeAmount: LiveData<List<MyTypes>>
    var readDifferenceSum: LiveData<Float>
    var readMonthlySpends: LiveData<Float>
    var incomeSum: LiveData<Float>
    var expenseSum: LiveData<Float>
    var readAccountDetails : LiveData<List<AccountDetails>>
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
        readTransactionTypeAmount = repository.amountCategory
        readAccountDetails = repository.readAccountDetails
    }

    val dates: MutableLiveData<List<Long>> = MutableLiveData()

    //    setting the detailed analysis list according to the dates
    val listAccordingToDate: LiveData<List<MyTypes>> = Transformations.switchMap(dates) {
        repository.getCustomDurationData(it[0], it[1])
    }
    val sumAccordingToDate: LiveData<Float> = Transformations.switchMap(dates) {
        repository.getCustomDurationDataSum(it[0], it[1])
    }

    //    setting the transactions list according to the dates
    val readAllTransactionDate: LiveData<List<Transaction>> = Transformations.switchMap(dates) {
        repository.getAllTransactionsByDate(it[0], it[1])
    }

    var transactionTypeDetails: MutableLiveData<TransactionTypeData> = MutableLiveData()

    //    transactions according to transactionstype and date asked
    val transactionsCategoryWise: LiveData<List<Transaction>> =
        Transformations.switchMap(transactionTypeDetails) {
            if (it.startDate == 0L) {
                repository.getMonthlyTransactionsData(it.transactionType)
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
    private var customData = MutableLiveData<customData>()

    val readCustomDefinedData:LiveData<List<Transaction>> = Transformations.switchMap(customData){
        repository.getCustomData(it.categoryList,it.accountList,it.monthList)
    }

    fun setCustomData(data:customData){
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
}

data class TransactionTypeData(
    val transactionType: String,
    val startDate: Long,
    val endDate: Long
)