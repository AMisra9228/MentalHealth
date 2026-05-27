package com.sample.mentalhealth.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.mentalhealth.data.dao.AccountDao
import com.sample.mentalhealth.data.dao.ItemDao
import com.sample.mentalhealth.data.entities.Info
import com.sample.mentalhealth.data.entities.Item
import com.sample.mentalhealth.login_registration.User
import com.sample.mentalhealth.login_registration.UserDao

@Database(
    entities =[Info::class, Item::class, User::class],
    version = 4,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun itemDao(): ItemDao
    abstract fun userDao(): UserDao

    companion object{

        @Volatile
        var  INSTANCE:TaskDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TaskDatabase{

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}