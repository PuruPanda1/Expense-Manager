package com.example.payment.cryptoDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CryptoTransaction::class], version = 1, exportSchema = false)
abstract class CryptoTransactionDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao

    companion object {
        @Volatile
        private var INSTANCE: CryptoTransactionDatabase? = null

        fun getInstance(context: Context): CryptoTransactionDatabase{
            var tempIns = INSTANCE
            if(tempIns != null)
            {
                return tempIns
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CryptoTransactionDatabase::class.java,
                    "crypto_transaction_database"
                ).build()
                return instance
            }
        }
    }
}