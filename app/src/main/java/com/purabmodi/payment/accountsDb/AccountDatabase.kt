package com.purabmodi.payment.accountsDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Accounts::class], version = 1, exportSchema = false)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountsDao

    companion object {
        @Volatile
        private var INSTANCE: AccountDatabase? = null
        fun getInstance(context: Context): AccountDatabase {
            val tempIns = INSTANCE
            if (tempIns != null) {
                return tempIns
            }
            synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext,
                    AccountDatabase::class.java,
                    "account_details_database"
                ).build()
            }
        }
    }
}