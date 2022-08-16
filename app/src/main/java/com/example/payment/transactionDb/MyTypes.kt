package com.example.payment.transactionDb

import androidx.room.ColumnInfo

data class MyTypes(
    @ColumnInfo(name="name")
    val name: String,
    @ColumnInfo(name="amount")
    val amount:Float,
    @ColumnInfo(name="count")
    val count:Int
)
