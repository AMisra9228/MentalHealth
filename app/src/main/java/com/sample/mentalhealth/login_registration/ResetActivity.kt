package com.sample.mentalhealth.login_registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.sample.mentalhealth.databinding.ForgotPasswordActivityBinding
import com.sample.mentalhealth.login_registration.viewmodel.ForgotPasswordViewModel

class ResetActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordActivityBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForgotPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Clear error when user starts typing
        binding.emailEditText.addTextChangedListener { editable ->
            val isEmpty = editable.isNullOrBlank()
            binding.resetPasswordButton.isEnabled = !isEmpty
            binding.resetPasswordButton.alpha = if (isEmpty) 0.5f else 1.0f

            if (!isEmpty) {
                viewModel.clearInputError()
            }
        }

        binding.resetPasswordButton.setOnClickListener {
            val emailOrUsername = binding.emailEditText.text.toString()
            viewModel.sendResetLink(emailOrUsername)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.resetPasswordButton.isEnabled = !isLoading
            binding.resetPasswordButton.text = if (isLoading) "" else "Send Reset Link"
        }

        // Validation error → show on TextInputLayout
        viewModel.inputError.observe(this) { error ->
            binding.emailInputLayout.error = error
            if (error != null) {
                binding.emailEditText.requestFocus()
            }
        }

        // API response → show as Toast
        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                binding.emailEditText.text?.clear()
                binding.resetPasswordButton.isEnabled = true
            }
        }
    }
}