package com.example.payment.transactionDb

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val readAllData: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val incomeData: LiveData<List<Transaction>> = transactionDao.getIncomeTransactions()
    val expenseData: LiveData<List<Transaction>> = transactionDao.getExpenseTransactions()
    val amountCategory: LiveData<List<myTypes>> = transactionDao.getTotal()
    val differenceSum: LiveData<Float> = transactionDao.getDifferenceSum()
    val monthlySpends: LiveData<Float> = transactionDao.getMonthlySpends()
    val incomeSum = transactionDao.getIncomeSum()
    val expenseSum = transactionDao.getExpenseSum()
    val readAccountDetails = transactionDao.getAccountDetails()

    fun getRangeTransactionsData(transactionType:String,startDate: Long,endDate: Long):LiveData<List<Transaction>>{
        return transactionDao.getRangeTransactionsData(transactionType,startDate,endDate)
    }

    fun getMonthlyTransactionsData(transactionType:String):LiveData<List<Transaction>>{
        return transactionDao.getMonthlyTransactionsData(transactionType)
    }

    fun getCustomDurationData(startDate: Long, endDate: Long): LiveData<List<myTypes>> {
        return transactionDao.getCustomDurationData(startDate, endDate)
    }

    fun getSingleTransactionType(transactionType:String,startDate: Long, endDate: Long): LiveData<myTypes> {
        return transactionDao.getSingleTransactionType(transactionType,startDate, endDate)
    }

    fun getMonthlySingleTransactionType(transactionType:String): LiveData<myTypes> {
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