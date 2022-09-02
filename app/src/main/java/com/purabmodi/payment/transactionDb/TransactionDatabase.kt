package com.purabmodi.payment.transactionDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Transaction::class], version = 2, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transacitonDao(): TransactionDao

    companion object {
        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE transactions ADD COLUMN year INTEGER NOT NULL DEFAULT(2022)")
            }
        }

        @Volatile
        private var INSTANCE: TransactionDatabase? = null
        fun getInstance(context: Context): TransactionDatabase {
            val tempIns = INSTANCE
            if (tempIns != null) {
                return tempIns
            }
            synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                )
                    .addMigrations(migration_1_2)
                    .build()
            }
        }
    }
}