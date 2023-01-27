package com.system.payment.userDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class User (
    @PrimaryKey(autoGenerate = false)
    val userId : Int,
    val username:String,
    val userBio: String,
    val userBudget:Int,
    val userPhoto: String,
    val userCurrency: String
        )