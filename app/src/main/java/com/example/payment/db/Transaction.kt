package com.example.payment.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val tDescription:String,
    val tAmount: Float,
    val isExpense:Boolean,
    val date: String,
    val transactionType:String,
    val remainingAmount: Float
)
