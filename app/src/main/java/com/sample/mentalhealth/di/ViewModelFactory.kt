// ViewModelFactory.kt
package com.sample.mentalhealth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.mentalhealth.login_registration.UserViewModelNew
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val userViewModelProvider: Provider<UserViewModelNew>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModelNew::class.java)) {
            return userViewModelProvider.get() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
