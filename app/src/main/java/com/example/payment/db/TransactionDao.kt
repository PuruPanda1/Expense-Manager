package com.example.payment.db

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

    @Query("SELECT * FROM transactions ORDER by id DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE isExpense=0 ORDER BY id DESC")
    fun getIncomeTransactions(): LiveData<List<Transaction>>

}