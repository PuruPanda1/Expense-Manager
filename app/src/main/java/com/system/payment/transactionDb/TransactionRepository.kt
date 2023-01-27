package com.system.payment.transactionDb

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val expensesList = listOf(
        "Credit Card Due",
        "Bills",
        "DineOut",
        "Entertainment",
        "Fuel",
        "Groceries",
        "Shopping",
        "Stationary",
        "General",
        "Transportation"
    )
    val incomeList = listOf(
        "Income",
        "Salary",
        "Updated Balance"
    )
    val readAllTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val readIncomeTransactions: LiveData<List<Transaction>> =
        transactionDao.getIncomeTransactions(incomeList)
    val readExpenseTransactions: LiveData<List<Transaction>> =
        transactionDao.getExpenseTransactions(expensesList)
    val differenceSum: LiveData<Float> = transactionDao.getDifferenceSum()
    val incomeSum = transactionDao.getIncomeSum(incomeList)
    val expenseSum = transactionDao.getExpenseSum(expensesList)
    val readAccountDetails = transactionDao.getAccountDetails()

    suspend fun deleteAccountTransactions(accountName: String) {
        transactionDao.deleteAccountTransactions(accountName)
    }

    fun readTransactionsByDay(day: Int, month: Int, year: Int): LiveData<List<Transaction>> {
        return transactionDao.readTransactionsByDay(day, month, year)
    }

    fun readExpenseByDay(day: Int,month: Int,year: Int):LiveData<Float>{
        return transactionDao.readExpenseByDay(day, month, year)
    }

    fun readIncomeByDay(day: Int,month: Int,year: Int):LiveData<Float>{
        return transactionDao.readIncomeByDay(day, month, year)
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

    fun getTransactionsByCategory(
        transactionType: String,
        month: Int
    ): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(transactionType, month)
    }

    fun readCategoriesByDuration(startDate: Long, endDate: Long): LiveData<List<MyTypes>> {
        return transactionDao.readCategoriesByDuration(startDate, endDate, expensesList)
    }

    fun readMonthlySpends(month: Int, year: Int): LiveData<Float> {
        return transactionDao.getMonthlySpends(month, year, expensesList)
    }

    fun readMonthlySumByCategory(month: Int, year: Int): LiveData<List<MyTypes>> {
        return transactionDao.getMonthlySpendByCategory(month, year, expensesList)
    }

    fun getSingleTransactionType(
        transactionType: String,
        month: Int
    ): LiveData<MyTypes> {
        return transactionDao.getSingleTransactionTypeByMonth(transactionType, month)
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