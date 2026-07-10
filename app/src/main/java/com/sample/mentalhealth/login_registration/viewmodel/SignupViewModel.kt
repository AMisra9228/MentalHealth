package com.sample.mentalhealth.login_registration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mentalhealth.common.PasswordStrength
import com.sample.mentalhealth.common.ValidationUtils
import com.sample.mentalhealth.login_registration.model.SignupRequest
import com.sample.mentalhealth.login_registration.repository.SignupRepository
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val repository = SignupRepository()

    // ----- Signup State -----
    private val _signupState = MutableLiveData<SignupState>(SignupState.Idle)
    val signupState: LiveData<SignupState> = _signupState

    // ----- Field Errors -----
    private val _fullNameError = MutableLiveData<String?>()
    val fullNameError: LiveData<String?> = _fullNameError

    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> = _usernameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _dobError = MutableLiveData<String?>()
    val dobError: LiveData<String?> = _dobError

    private val _genderError = MutableLiveData<String?>()
    val genderError: LiveData<String?> = _genderError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _confirmPasswordError = MutableLiveData<String?>()
    val confirmPasswordError: LiveData<String?> = _confirmPasswordError

    private val _termsError = MutableLiveData<String?>()
    val termsError: LiveData<String?> = _termsError

    // ----- Password Strength -----
    private val _passwordStrength = MutableLiveData(PasswordStrength.NONE)
    val passwordStrength: LiveData<PasswordStrength> = _passwordStrength

    // ------------------------------------------------------------------

    fun updatePasswordStrength(password: String) {
        _passwordStrength.value = ValidationUtils.getPasswordStrength(password)
    }

    // ------------------------------------------------------------------

    fun validateAndSignup(
        fullName: String,
        username: String,
        email: String,
        dob: String,
        genderId: Int,
        genderText: String,
        password: String,
        confirmPassword: String,
        termsChecked: Boolean
    ) {
        var isValid = true

        // --- Full Name ---
        if (fullName.isBlank()) {
            _fullNameError.value = "Full name is required"
            isValid = false
        } else if (!ValidationUtils.isValidFullName(fullName)) {
            _fullNameError.value = "Enter a valid full name (min 3 letters)"
            isValid = false
        } else {
            _fullNameError.value = null
        }

        // --- Username ---
        if (username.isBlank()) {
            _usernameError.value = "Username is required"
            isValid = false
        }
        /*else if (!ValidationUtils.isValidUsername(username)) {
            _usernameError.value = "Min 3 chars, letters/numbers/_/. only"
            isValid = false
        }*/
        else {
            _usernameError.value = null
        }

        // --- Email ---
        if (email.isBlank()) {
            _emailError.value = "Email is required"
            isValid = false
        } else if (!ValidationUtils.isValidEmail(email)) {
            _emailError.value = "Enter a valid email address"
            isValid = false
        } else {
            _emailError.value = null
        }

        // --- DOB ---
        if (dob.isBlank()) {
            _dobError.value = "Date of birth is required"
            isValid = false
        } else if (!ValidationUtils.isValidDob(dob)) {
            _dobError.value = "Enter a valid date (YYYY-MM-DD)" // <-- Updated message
            isValid = false
        } else {
            _dobError.value = null
        }

        // --- Gender ---
        if (genderId == -1) {
            _genderError.value = "Please select your gender"
            isValid = false
        } else {
            _genderError.value = null
        }

        // --- Password ---
        if (password.isBlank()) {
            _passwordError.value = "Password is required"
            isValid = false
        } else if (!ValidationUtils.isValidPassword(password)) {
            _passwordError.value = "Min 8 chars with letters and numbers"
            isValid = false
        } else {
            _passwordError.value = null
        }

        // --- Confirm Password ---
        if (confirmPassword.isBlank()) {
            _confirmPasswordError.value = "Please confirm your password"
            isValid = false
        } else if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
            _confirmPasswordError.value = "Passwords do not match"
            isValid = false
        } else {
            _confirmPasswordError.value = null
        }

        // --- Terms ---
        if (!termsChecked) {
            _termsError.value = "Please accept the Terms of Service"
            isValid = false
        } else {
            _termsError.value = null
        }

        if (!isValid) return

        // ----- All valid — make API call -----
        viewModelScope.launch {
            _signupState.value = SignupState.Loading

            val request = SignupRequest(
                full_name = fullName.trim(),
                username = username.trim(),
                email = email.trim(),
                dob = dob.trim(),
                gender = genderText,
                password = password,
                confirm_password = confirmPassword
            )

            val result = repository.signup(request)

            result.onSuccess { response ->
                if (response.success) {
                    _signupState.value = SignupState.Success(response.message)
                } else {
                    _signupState.value = SignupState.Error(response.message)
                }
            }.onFailure { error ->
                _signupState.value = SignupState.Error(
                    error.message ?: "Something went wrong. Please try again."
                )
            }
        }
    }

    fun resetState() {
        _signupState.value = SignupState.Idle
    }
}

// ------------------------------------------------------------------

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    data class Success(val message: String) : SignupState()
    data class Error(val message: String) : SignupState()
}