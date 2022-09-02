package com.purabmodi.payment.transactionDb

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER by date DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isExpense=0 ORDER BY date DESC")
    fun getIncomeTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isExpense=1 ORDER BY date DESC")
    fun getExpenseTransactions(): LiveData<List<Transaction>>

    @Query("SELECT SUM(incomeAmount)-SUM(expenseAmount) FROM transactions")
    fun getDifferenceSum(): LiveData<Float>

    @Query("SELECT SUM(expenseAmount) FROM transactions WHERE month = :month AND year = :year")
    fun getMonthlySpends(month:Int,year:Int): LiveData<Float>

    @Query("SELECT SUM(incomeAmount) FROM transactions")
    fun getIncomeSum(): LiveData<Float>

    @Query("SELECT SUM(expenseAmount) FROM transactions")
    fun getExpenseSum(): LiveData<Float>

    @Query("SELECT transactionType as name,Count(*) as count,SUM(expenseAmount) as amount FROM transactions WHERE isExpense=1 AND month = :month AND year = :year GROUP BY transactionType ORDER BY amount ASC")
    fun getMonthlySpendByCategory(month: Int,year: Int): LiveData<List<MyTypes>>

    @Query("SELECT transactionType as name,Count(*) as count,SUM(expenseAmount) as amount FROM transactions WHERE isExpense=1 AND date BETWEEN :startDate AND :endDate GROUP BY transactionType")
    fun readCategoriesByDuration(startDate: Long, endDate: Long): LiveData<List<MyTypes>>

    @Query("SELECT SUM(expenseAmount) FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    fun readExpenseSumByDuration(startDate: Long, endDate: Long): LiveData<Float>

    @Query("SELECT * FROM transactions WHERE transactionType=:transactionType AND month = strftime('%m','now') ORDER BY date DESC")
    fun getTransactionsByCategory(transactionType: String): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE transactionType=:transactionType AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getRangeTransactionsData(
        transactionType: String,
        startDate: Long,
        endDate: Long
    ): LiveData<List<Transaction>>

    @Query("SELECT transactionType as name,Count(*) as count,SUM(expenseAmount) as amount FROM transactions WHERE transactionType = :transactionType AND date BETWEEN :startDate AND :endDate GROUP BY transactionType")
    fun getSingleTransactionType(
        transactionType: String,
        startDate: Long,
        endDate: Long
    ): LiveData<MyTypes>

    @Query("SELECT transactionType as name,Count(*) as count,SUM(expenseAmount) as amount FROM transactions WHERE transactionType = :transactionType AND month = strftime('%m','now') GROUP BY transactionType")
    fun getMonthlySingleTransactionType(transactionType: String): LiveData<MyTypes>

    @Query("SELECT modeOfPayment as accountName,SUM(incomeAmount)-SUM(expenseAmount) as accountBalance FROM transactions GROUP BY modeOfPayment ORDER BY modeOfPayment ASC")
    fun getAccountDetails(): LiveData<List<AccountDetails>>

    //    custom filtering
    @Query("SELECT * FROM transactions WHERE transactionType IN (:categoryList) AND modeOfPayment IN (:accountList) AND month IN (:monthList) ORDER BY date DESC")
    fun readTransactionsByMonth(
        categoryList: List<String>,
        accountList: List<String>,
        monthList: List<Int>
    ): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE transactionType IN (:categoryList) AND modeOfPayment IN (:accountList) AND date BETWEEN (:startDate) AND (:endDate) ORDER BY date DESC")
    fun readTransactionsByDuration(
        categoryList: List<String>,
        accountList: List<String>,
        startDate: Long,
        endDate: Long
    ): LiveData<List<Transaction>>

    @Query("DELETE FROM transactions WHERE modeOfPayment = :accountName")
    fun deleteAccountTransactions(accountName: String)

}