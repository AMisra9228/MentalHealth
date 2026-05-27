package com.sample.mentalhealth.login_registration

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.sample.mentalhealth.databinding.ActivityResetBinding

class ResetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        binding.resetPasswordButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailEditText.error = "Email is required"
                return@setOnClickListener
            }

            viewModel.sendPasswordReset(email)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.resetStatus.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                finish() // go back to login
            }
            result.onFailure {
                Toast.makeText(this, it.localizedMessage ?: "Error occurred", Toast.LENGTH_LONG).show()
            }
        }
    }
}