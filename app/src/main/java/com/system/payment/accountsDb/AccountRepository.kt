package com.system.payment.accountsDb

import androidx.lifecycle.LiveData

class AccountRepository(private val accountsDao: AccountsDao) {
    val readAllAccounts: LiveData<List<Accounts>> = accountsDao.getAllAccounts()

    suspend fun insertAccount(accounts: Accounts) {
        accountsDao.insertAccount(accounts)
    }

    suspend fun updateAccount(accounts: Accounts) {
        accountsDao.updateAccount(accounts)
    }

    suspend fun deleteAccount(accounts: Accounts) {
        accountsDao.deleteAccount(accounts)
    }

    fun deleteAccountWithName(accountName: String) {
        accountsDao.deleteAccountWithName(accountName)
    }
}