package com.example.payment.transactionDb

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val readAllTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val readIncomeTransactions: LiveData<List<Transaction>> = transactionDao.getIncomeTransactions()
    val readExpenseTransactions: LiveData<List<Transaction>> =
        transactionDao.getExpenseTransactions()
    val readSumByCategory: LiveData<List<MyTypes>> = transactionDao.getTotal()
    val differenceSum: LiveData<Float> = transactionDao.getDifferenceSum()
    val monthlySpends: LiveData<Float> = transactionDao.getMonthlySpends()
    val incomeSum = transactionDao.getIncomeSum()
    val expenseSum = transactionDao.getExpenseSum()
    val readAccountDetails = transactionDao.getAccountDetails()

    suspend fun deleteAccountTransactions(accountName: String) {
        transactionDao.deleteAccountTransactions(accountName)
    }

    fun readTransactionsByMonth(
        categoryList: List<String>,
        accountList: List<String>,
        monthList: List<Int>
    ): LiveData<List<Transaction>> {
        return transactionDao.readTransactionsByMonth(categoryList, accountList, monthList)
    }

    fun readTransactionsByDuration(
        categoryList: List<String>,
        accountList: List<String>,
        startDate: Long,
        endDate: Long
    ): LiveData<List<Transaction>> {
        return transactionDao.readTransactionsByDuration(
            categoryList,
            accountList,
            startDate,
            endDate
        )
    }


    fun getRangeTransactionsData(
        transactionType: String,
        startDate: Long,
        endDate: Long
    ): LiveData<List<Transaction>> {
        return transactionDao.getRangeTransactionsData(transactionType, startDate, endDate)
    }

    fun getTransactionsByCategory(transactionType: String): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(transactionType)
    }

    fun readCategoriesByDuration(startDate: Long, endDate: Long): LiveData<List<MyTypes>> {
        return transactionDao.readCategoriesByDuration(startDate, endDate)
    }

    fun getSingleTransactionType(
        transactionType: String,
        startDate: Long,
        endDate: Long
    ): LiveData<MyTypes> {
        return transactionDao.getSingleTransactionType(transactionType, startDate, endDate)
    }

    fun getMonthlySingleTransactionType(transactionType: String): LiveData<MyTypes> {
        return transactionDao.getMonthlySingleTransactionType(transactionType)
    }

    fun readExpenseSumByDuration(startDate: Long, endDate: Long): LiveData<Float> {
        return transactionDao.readExpenseSumByDuration(startDate, endDate)
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