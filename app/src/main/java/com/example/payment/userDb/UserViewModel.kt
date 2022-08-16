package com.example.payment.userDb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {
    val userDetails : LiveData<User>
    val userBudget : LiveData<Int>
    private val repository : UserRepository

    init {
        val dao = UserDatabase.getInstance(application).UserDao()
        repository = UserRepository(dao)
        userBudget = repository.userBudget
        userDetails = repository.userDetails
    }

    fun insertUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun updateUser(user:User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }
}