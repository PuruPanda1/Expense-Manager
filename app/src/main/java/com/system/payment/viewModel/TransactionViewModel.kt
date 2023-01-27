package com.system.payment.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.system.payment.modals.CustomTimeData
import com.system.payment.modals.customData
import com.system.payment.transactionDb.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    var readAllTransaction: LiveData<List<Transaction>>
    var readAllIncomeTransaction: LiveData<List<Transaction>>
    var readAllExpenseTransaction: LiveData<List<Transaction>>
    var readDifferenceSum: LiveData<Float>
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
        incomeSum = repository.incomeSum
        expenseSum = repository.expenseSum
        readAccountDetails = repository.readAccountDetails
    }

    var monthYear: MutableLiveData<List<Int>> = MutableLiveData(
        listOf(
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.YEAR)
        )
    )

    var dayOfYear: MutableLiveData<List<Int>> = MutableLiveData(listOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR),Calendar.getInstance().get(Calendar.YEAR)))

    fun setDayOfYear(dayOfYear:List<Int>){
        this.dayOfYear.value = dayOfYear
    }

    var readTransactionsByDay:LiveData<List<Transaction>> = Transformations.switchMap(dayOfYear){
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_YEAR,it[0])
        cal.set(Calendar.YEAR,it[1])
        repository.readTransactionsByDay(cal.get(Calendar.DAY_OF_MONTH),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.YEAR))
    }

    var readExpenseByDay:LiveData<Float> = Transformations.switchMap(dayOfYear){
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_YEAR,it[0])
        cal.set(Calendar.YEAR,it[1])
        repository.readExpenseByDay(cal.get(Calendar.DAY_OF_MONTH),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.YEAR))
    }

    var readIncomeByDay:LiveData<Float> = Transformations.switchMap(dayOfYear){
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_YEAR,it[0])
        cal.set(Calendar.YEAR,it[1])
        repository.readIncomeByDay(cal.get(Calendar.DAY_OF_MONTH),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.YEAR))
    }

    fun setMonthYear(monthYear: List<Int>) {
        this.monthYear.value = monthYear
    }

    val readMonthlySpends: LiveData<Float> = Transformations.switchMap(monthYear) {
        repository.readMonthlySpends(it[0], it[1])
    }

    val readMonthlySumByCategory: LiveData<List<MyTypes>> = Transformations.switchMap(monthYear) {
        repository.readMonthlySumByCategory(it[0], it[1])
    }

    var categoryName: MutableLiveData<String> = MutableLiveData()

    val readTransactionsByCategory: LiveData<List<Transaction>> =
        Transformations.switchMap(categoryName) {
            repository.getTransactionsByCategory(it, monthYear.value!!.get(0))
        }

    val readSingleTransactionType: LiveData<MyTypes> =
        Transformations.switchMap(categoryName) {
            repository.getSingleTransactionType(it, monthYear.value!!.get(0))
        }

    fun setCategoryName(name: String) {
        categoryName.value = name
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
    val month: Int
)