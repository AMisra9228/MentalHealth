package com.sample.mentalhealth.login_registration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mentalhealth.login_registration.SingleLiveEvent
import com.sample.mentalhealth.login_registration.repository.ForgotPasswordRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: ForgotPasswordRepository = ForgotPasswordRepository()
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: SingleLiveEvent<String> = _toastMessage

    private val _inputError = MutableLiveData<String?>()
    val inputError: LiveData<String?> = _inputError

    private val _navigateToOtp = SingleLiveEvent<Boolean>()
    val navigateToOtp: SingleLiveEvent<Boolean> = _navigateToOtp

    fun sendResetLink(emailOrUsername: String) {
        // Clear previous error
        _inputError.value = null

        if (!validateInput(emailOrUsername)) return

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.sendResetLink(emailOrUsername.trim())
            _isLoading.value = false

            result.onSuccess { message ->
                _toastMessage.value = message
                _navigateToOtp.value = true
            }.onFailure { error ->
                _toastMessage.value = error.message
            }
        }
    }

    private fun validateInput(emailOrUsername: String): Boolean {
        val trimmed = emailOrUsername.trim()
        return when {
            trimmed.isBlank() -> {
                _inputError.value = "Please enter your email or username"
                false
            }
            trimmed.contains("@") && !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> {
                _inputError.value = "Please enter a valid email address"
                false
            }
            trimmed.length < 3 -> {
                _inputError.value = "Username must be at least 3 characters"
                false
            }
            else -> true
        }
    }

    fun clearInputError() {
        _inputError.value = null
    }
}