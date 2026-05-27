package com.sample.mentalhealth.login_registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _resetStatus = MutableLiveData<Result<String>>()
    val resetStatus: LiveData<Result<String>> get() = _resetStatus

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            _resetStatus.value = result
        }
    }
}