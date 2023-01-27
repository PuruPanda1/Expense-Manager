package com.purabmodi.payment.userDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT userBudget FROM user_details WHERE userId = 1")
    fun getUserBudget():LiveData<Int>

    @Query("SELECT * FROM user_details WHERE userId=1")
    fun getUserDetails(): LiveData<User>
}