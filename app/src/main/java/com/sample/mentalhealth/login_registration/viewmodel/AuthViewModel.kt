package com.sample.mentalhealth.login_registration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mentalhealth.common.ValidationUtils
import com.sample.mentalhealth.login_registration.repository.AuthRepository
import com.sample.mentalhealth.login_registration.model.User

import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // Sign-in state
    private val _signInResult = MutableLiveData<ResultState<User>>()
    val signInResult: LiveData<ResultState<User>> = _signInResult

    // Validation error
    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

    // Loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signIn(email: String, password: String, rememberMe: Boolean) {
        // Validate inputs first
        val error = ValidationUtils.validateSignIn(email, password)
        if (error != null) {
            _validationError.value = error
            return
        }
        _validationError.value = null

        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.signIn(email, password)
            _isLoading.value = false

            result.fold(
                onSuccess = { response ->
                    response.user?.let { user ->
                        _signInResult.value = ResultState.Success(user, rememberMe)
                    } ?: run {
                        _signInResult.value = ResultState.Error("User data not found")
                    }
                },
                onFailure = { exception ->
                    _signInResult.value = ResultState.Error(exception.message ?: "Unknown error occurred")
                }
            )
        }
    }

    fun clearValidationError() {
        _validationError.value = null
    }

    sealed class ResultState<out T> {
        data class Success<out T>(val data: T, val rememberMe: Boolean) : ResultState<T>()
        data class Error(val message: String) : ResultState<Nothing>()
        object Loading : ResultState<Nothing>()
    }
}