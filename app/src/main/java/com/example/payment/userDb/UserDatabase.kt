package com.example.payment.userDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase :RoomDatabase(){
    abstract fun UserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase {
            var tempIns = INSTANCE
            if (tempIns != null) {
                return tempIns
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_details"
                ).build()
                return instance
            }
        }
    }
}