package com.sample.mentalhealth.login_registration

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun getUser(context:Context,email: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmailAndPassword(email, password)
        }
    }

    suspend fun getRegisteredUser(): User? {
        return userDao.getRegisteredUser()
    }

    suspend fun getUserNameByEmail(email: String): String? {
        return userDao.getUserNameByEmail(email)
    }
}
