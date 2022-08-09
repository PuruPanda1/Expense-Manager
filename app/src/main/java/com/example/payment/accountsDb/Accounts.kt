package com.example.payment.accountsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_details")
data class Accounts(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String
)
