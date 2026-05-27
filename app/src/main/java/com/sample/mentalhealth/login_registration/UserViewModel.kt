package com.sample.mentalhealth.login_registration

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel()  {
    fun insertUserInfo(context: Context,userName: String, userEmail: String, userPassword: String, userDt: String) {

        CoroutineScope(Dispatchers.IO).launch {
            UserRepo.insertUser(context,userName,userEmail,userPassword,userDt)
        }
    }

    suspend fun getUserInfo(context: Context,userEmail: String, userPassword: String) : Boolean {
            return UserRepo.getUser(context,userEmail,userPassword)
    }
}