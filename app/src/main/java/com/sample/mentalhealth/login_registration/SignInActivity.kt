package com.sample.mentalhealth.login_registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.sample.mentalhealth.MainActivity
import com.sample.mentalhealth.common.SessionManager
import com.sample.mentalhealth.databinding.ActivityLoginNewBinding
import com.sample.mentalhealth.home.HomeFragment
import com.sample.mentalhealth.login_registration.viewmodel.AuthViewModel
import com.sample.mentalhealth.login_registration.viewmodel.AuthViewModelFactory
import com.sample.mentalhealth.login_registration.repository.AuthRepository


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginNewBinding
    private lateinit var sessionManager: SessionManager

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SessionManager first to check login status
        sessionManager = SessionManager(this)

        // Skip if already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToHome()
            finish()
            return // Stop further execution since we are navigating away
        }

        binding = ActivityLoginNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
        preFillRememberedEmail()
    }

    private fun setupListeners() {
        // Sign In button
        binding.btnSignIn.setOnClickListener {
            val email = binding.atvEmailLog.text.toString().trim()
            val password = binding.atvPasswordLog.text.toString().trim()
            val rememberMe = binding.cbRememberMe.isChecked

            viewModel.signIn(email, password, rememberMe)
        }

        // Forgot password
        binding.tvForgotPass.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Sign Up
        binding.tvSignUp.setOnClickListener {
            // startActivity(Intent(this, SignUpActivity::class.java))
            Toast.makeText(this, "Navigate to Sign Up", Toast.LENGTH_SHORT).show()
        }

        // Google
        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Google Sign-In", Toast.LENGTH_SHORT).show()
            // TODO: Initiate GoogleSignIn flow
        }

        // Facebook
        binding.btnFacebook.setOnClickListener {
            Toast.makeText(this, "Facebook Sign-In", Toast.LENGTH_SHORT).show()
            // TODO: Initiate Facebook Login flow
        }

        // Clear validation error on text change (Cleaned up using KTX extensions)
        binding.atvEmailLog.doAfterTextChanged { viewModel.clearValidationError() }
        binding.atvPasswordLog.doAfterTextChanged { viewModel.clearValidationError() }
    }

    private fun observeViewModel() {
        // Loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSignIn.isEnabled = !isLoading
            binding.btnSignIn.text = if (isLoading) "Signing In..." else "Sign In"
        }

        // Validation error
        viewModel.validationError.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Sign-in result
        viewModel.signInResult.observe(this) { state ->
            when (state) {
                is AuthViewModel.ResultState.Loading -> {
                    // Already handled by isLoading
                }
                is AuthViewModel.ResultState.Success -> {
                    // We don't even need to explicitly declare 'val user: User' anymore.
                    // Kotlin smart-casts 'state.data' to User automatically.
                    val user = state.data
                    val rememberMe = state.rememberMe

                    // Save session
                    sessionManager.saveUserSession(
                        token = user.token,
                        userId = user.id,
                        username = user.username,
                        email = user.email,
                        rememberMe = rememberMe
                    )

                    // Save remembered email
                    sessionManager.saveRememberedEmail(user.email, rememberMe)

                    Toast.makeText(this, "Welcome ${user.username}!", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                    finish()
                }

                is AuthViewModel.ResultState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun preFillRememberedEmail() {
        sessionManager.getRememberedEmail()?.let { email ->
            binding.atvEmailLog.setText(email)
            binding.cbRememberMe.isChecked = true
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        Toast.makeText(this, "Navigate to Home Screen", Toast.LENGTH_SHORT).show()
    }
}