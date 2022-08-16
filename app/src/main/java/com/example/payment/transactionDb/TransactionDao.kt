package com.example.payment.transactionDb

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

    @Query("SELECT SUM(expenseAmount) FROM transactions WHERE month = strftime('%m', 'now')")
    fun getMonthlySpends(): LiveData<Float>

    @Query("SELECT SUM(incomeAmount) FROM transactions WHERE month = strftime('%m', 'now')")
    fun getIncomeSum(): LiveData<Float>

    @Query("SELECT SUM(expenseAmount) FROM transactions WHERE month = strftime('%m', 'now')")
    fun getExpenseSum(): LiveData<Float>

    @Query("SELECT transactionType as name,Count(*) as count,SUM(expenseAmount) as amount FROM transactions WHERE isExpense=1 AND month = strftime('%m','now') GROUP BY transactionType")
    fun getTotal(): LiveData<List<MyTypes>>

    @Query("SELECT transactionType as name,Count(*) as count,SUM(expenseAmount) as amount FROM transactions WHERE isExpense=1 AND date BETWEEN :startDate AND :endDate GROUP BY transactionType")
    fun getCustomDurationData(startDate: Long, endDate: Long): LiveData<List<MyTypes>>

    @Query("SELECT SUM(expenseAmount) FROM transactions WHERE isExpense=1 AND date BETWEEN :startDate AND :endDate")
    fun getCustomDurationDataSum(startDate: Long, endDate: Long): LiveData<Float>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getAllTransactionsByDate(startDate: Long, endDate: Long): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isExpense=1 AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getAllExpenseTransactionsByDate(startDate: Long, endDate: Long): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isExpense=0 AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getAllIncomeTransactionsByDate(startDate: Long, endDate: Long): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE transactionType=:transactionType AND month = strftime('%m','now') ORDER BY date DESC")
    fun getMonthlyTransactionsData(transactionType: String): LiveData<List<Transaction>>

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
}