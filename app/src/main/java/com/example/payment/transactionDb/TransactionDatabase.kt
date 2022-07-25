package com.example.payment.transactionDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transacitonDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null
        fun getInstance(context: Context): TransactionDatabase {
            var tempIns = INSTANCE
            if (tempIns != null) {
                return tempIns
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                ).build()
                return instance
            }
        }
    }
}