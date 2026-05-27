package com.sample.mentalhealth.login_registration

import android.content.Context
import com.sample.mentalhealth.data.TaskDatabase

class UserRepo {

    companion object {
        var userDatabase : TaskDatabase ?= null
        fun initializeDB(context: Context) : TaskDatabase {
            return TaskDatabase.getInstance(context)
        }

        suspend fun insertUser(context: Context,userName: String, userEmail: String, userPassword: String, userDt: String) {
            userDatabase = initializeDB(context)
            val user = User(userName, userEmail, userPassword, userDt,true)
            userDatabase!!.userDao().insertUser(user)
        }

        suspend fun getUser(context: Context, userEmail: String, userPassword: String) : Boolean {
            userDatabase = initializeDB(context)
            return userDatabase!!.userDao().getUser(userEmail, userPassword)
        }
    }
}