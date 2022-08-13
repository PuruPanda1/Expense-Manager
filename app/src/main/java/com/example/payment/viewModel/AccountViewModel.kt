package com.example.payment.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.payment.accountsDb.AccountDatabase
import com.example.payment.accountsDb.AccountRepository
import com.example.payment.accountsDb.Accounts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(application: Application):AndroidViewModel(application) {
    private val repository:AccountRepository
    val readAllAccounts:LiveData<List<Accounts>>

    init {
        val dao = AccountDatabase.getInstance(application).accountDao()
        repository = AccountRepository(dao)
        readAllAccounts = repository.readAllAccounts
    }

    fun insertAccount(accounts: Accounts){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAccount(accounts)
        }
    }

    fun updateAccount(accounts: Accounts){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAccount(accounts)
        }
    }

    fun deleteAccount(accounts: Accounts){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAccount(accounts)
        }
    }
}