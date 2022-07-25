package com.example.payment.cryptoDb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.payment.fragments.wallet.cryptoList.cryptoListFragment

@Dao
interface CryptoDao {
    @Insert
    suspend fun insertCryptoTransaction(cryptoTransaction: CryptoTransaction)

    @Update
    suspend fun updateCryptoTransaction(cryptoTransaction: CryptoTransaction)

    @Delete
    suspend fun deleteCryptoTransaction(cryptoTransaction: CryptoTransaction)

    @Query("SELECT * FROM cryptoTransaction ORDER BY date")
    fun getAllStudents(): LiveData<List<CryptoTransaction>>
}