package com.sample.mentalhealth.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.mentalhealth.data.entities.Info
import com.sample.mentalhealth.login_registration.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {

    fun getUserData(): LiveData<User> = repository.getUser()

    fun insertData(userName: String, userMail: String) {
        val info = Info(userName, userMail)

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(info)
        }
    }
}