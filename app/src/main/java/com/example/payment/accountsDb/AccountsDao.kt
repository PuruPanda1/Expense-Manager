package com.example.payment.accountsDb

import android.accounts.Account
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AccountsDao {

    @Insert
    suspend fun insertAccount(accounts: Accounts)

    @Update
    suspend fun updateAccount(accounts: Accounts)

    @Delete
    suspend fun deleteAccount(accounts: Accounts)

    @Query("SELECT * FROM account_details")
    fun getAllAccounts():LiveData<List<Accounts>>

}