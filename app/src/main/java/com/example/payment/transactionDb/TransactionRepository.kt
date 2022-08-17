package com.example.payment.transactionDb

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val readAllData: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val incomeData: LiveData<List<Transaction>> = transactionDao.getIncomeTransactions()
    val expenseData: LiveData<List<Transaction>> = transactionDao.getExpenseTransactions()
    val amountCategory: LiveData<List<MyTypes>> = transactionDao.getTotal()
    val differenceSum: LiveData<Float> = transactionDao.getDifferenceSum()
    val monthlySpends: LiveData<Float> = transactionDao.getMonthlySpends()
    val incomeSum = transactionDao.getIncomeSum()
    val expenseSum = transactionDao.getExpenseSum()
    val readAccountDetails = transactionDao.getAccountDetails()

    fun getCustomData(categoryList:List<String>,accountList:List<String>,monthList:List<Int>):LiveData<List<Transaction>>{
        return transactionDao.getCustomData(categoryList,accountList,monthList)
    }

    fun getRangeTransactionsData(transactionType:String,startDate: Long,endDate: Long):LiveData<List<Transaction>>{
        return transactionDao.getRangeTransactionsData(transactionType,startDate,endDate)
    }

    fun getMonthlyTransactionsData(transactionType:String):LiveData<List<Transaction>>{
        return transactionDao.getMonthlyTransactionsData(transactionType)
    }

    fun getCustomDurationData(startDate: Long, endDate: Long): LiveData<List<MyTypes>> {
        return transactionDao.getCustomDurationData(startDate, endDate)
    }

    fun getSingleTransactionType(transactionType:String,startDate: Long, endDate: Long): LiveData<MyTypes> {
        return transactionDao.getSingleTransactionType(transactionType,startDate, endDate)
    }

    fun getMonthlySingleTransactionType(transactionType:String): LiveData<MyTypes> {
        return transactionDao.getMonthlySingleTransactionType(transactionType)
    }

    fun getCustomDurationDataSum(startDate: Long, endDate: Long): LiveData<Float> {
        return transactionDao.getCustomDurationDataSum(startDate, endDate)
    }

    fun getAllTransactionsByDate(startDate: Long,endDate: Long): LiveData<List<Transaction>>{
        return transactionDao.getAllTransactionsByDate(startDate, endDate)
    }
    fun getAllIncomeTransactionsByDate(startDate: Long,endDate: Long): LiveData<List<Transaction>>{
        return transactionDao.getAllIncomeTransactionsByDate(startDate, endDate)
    }
    fun getAllExpenseTransactionsByDate(startDate: Long,endDate: Long): LiveData<List<Transaction>>{
        return transactionDao.getAllExpenseTransactionsByDate(startDate, endDate)
    }

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