package com.sample.mentalhealth.login_registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.mentalhealth.databinding.ForgotPasswordActivityBinding

class ResetActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForgotPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetPasswordButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailEditText.error = "Email is required"
                return@setOnClickListener
            }
        }
    }
}