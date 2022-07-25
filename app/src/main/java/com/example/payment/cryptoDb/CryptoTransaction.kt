package com.example.payment.cryptoDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptoTransaction")
data class CryptoTransaction(
    @PrimaryKey(autoGenerate = true)
    val sid: Int,
    val id: String,
    val name: String,
    val numberOfCoins: Double,
    val buyingPrice: Double,
    val date: Long
)