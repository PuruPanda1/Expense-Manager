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
    fun getMonthlySpends():LiveData<Float>

    @Query("SELECT SUM(incomeAmount) FROM transactions WHERE isExpense=0 AND month = strftime('%m','now')")
    fun getIncomeSum(): LiveData<Float>

    @Query("SELECT SUM(expenseAmount) FROM transactions WHERE isExpense=1 AND month = strftime('%m','now')")
    fun getExpenseSum(): LiveData<Float>
}