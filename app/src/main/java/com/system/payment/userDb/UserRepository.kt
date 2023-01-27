package com.system.payment.userDb

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    val userBudget : LiveData<Int> = userDao.getUserBudget()
    val userDetails : LiveData<User> = userDao.getUserDetails()

    suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }
}