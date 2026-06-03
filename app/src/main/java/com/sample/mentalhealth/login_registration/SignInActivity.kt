package com.sample.mentalhealth.login_registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sample.mentalhealth.MainActivity
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.R
import com.sample.mentalhealth.databinding.ActivityLoginNewBinding
import com.sample.mentalhealth.di.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {
    lateinit var context: Context

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: UserViewModelNew

    private lateinit var binding: ActivityLoginNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this@SignInActivity

        // Inject dependencies
        (application as MyApp).appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModelNew::class.java]

        binding.btnSignIn.setOnClickListener {

            val userEmail = binding.atvEmailLog.text.toString()
            val userPassword = binding.atvPasswordLog.text.toString()

            if (binding.atvEmailLog.text.toString().isEmpty()) {
                binding.atvEmailLog.error = getString(R.string.email_req)
            } else if (binding.atvPasswordLog.text.toString().isEmpty()) {
                binding.atvPasswordLog.error = getString(R.string.passwordReg)
            } else {
                lifecycleScope.launch(Dispatchers.Main) {

                    try {
                        // Call your suspend functions here
                        val status = viewModel.getUserInfo(context, userEmail, userPassword)

                        if (status) { // Assuming getUserInfo returns true on success
                            val sharedPreferences = getSharedPreferences("UserLog", MODE_PRIVATE)
                            sharedPreferences.edit {
                                putString("user_name", binding.atvEmailLog.text.toString().trim())
                                putString("pwd", binding.atvPasswordLog.text.toString().trim())
                                apply()
                            }
                            Toast.makeText(
                                this@SignInActivity,
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val i = Intent(context, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Toast.makeText(
                                this@SignInActivity,
                                "Invalid Credentials",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("Error", "Database Error: ${e.message}")
                    }
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            val i = Intent(context, SignUpActivity::class.java)
            startActivity(i)
        }

        binding.tvForgotPass.setOnClickListener {
            val i = Intent(context, ResetActivity::class.java)
            startActivity(i)
        }
    }
}