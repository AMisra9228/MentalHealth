package com.sample.mentalhealth.login_registration

import android.content.Context
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class UserViewModelNew @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    suspend fun getUserInfo(context: Context, email: String, password: String): Boolean {
        val user = repository.getUser(context,email, password)
        return user != null
    }
}