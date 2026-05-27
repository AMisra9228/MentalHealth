package com.sample.mentalhealth.account

import androidx.lifecycle.LiveData
import com.sample.mentalhealth.data.dao.AccountDao
import com.sample.mentalhealth.data.entities.Info
import com.sample.mentalhealth.login_registration.User

class AccountRepository(private val accountDao: AccountDao)  {

    suspend fun insert(accinfo: Info) {
        accountDao.insertAccount(accinfo)
    }

    fun getUser(): LiveData<User> {
        return accountDao.getUser()
    }
}