package com.example.payment.transactionDb

import androidx.room.ColumnInfo

data class AccountDetails(
    @ColumnInfo(name="accountName")
    val accountName:String,
    @ColumnInfo(name="accountBalance")
    val accountBalance:Float
)
