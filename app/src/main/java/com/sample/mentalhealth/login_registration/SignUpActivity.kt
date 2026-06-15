package com.sample.mentalhealth.login_registration

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sample.mentalhealth.MainActivity
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.databinding.ActvySignupBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class SignUpActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActvySignupBinding
    private lateinit var viewModel: UserViewModel
    private var locTime: String = ""

    val countryCodes = arrayOf(
        "🇮🇳 +91",
        "🇺🇸 +1",
        "🇬🇧 +44",
        "🇦🇺 +61",
        "🇨🇦 +1",
        "🇩🇪 +49",
        "🇫🇷 +33",
        "🇯🇵 +81"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActvySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            countryCodes
        )

        binding.spinnerCountryCode.adapter = adapter

        val selectedCode = binding.spinnerCountryCode.selectedItem.toString()
        val mobile = binding.edtMobile.text.toString()

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val savedName = sharedPreferences.getString("user_name", "")
        val savedPwd = sharedPreferences.getString("pwd", "")

        lifecycleScope.launch {

            val user = viewModel.getRegisteredUser(this@SignUpActivity)

            if (user != null && (!savedName.equals("") && !savedPwd.equals(""))) {
                startActivity(
                    Intent(
                        this@SignUpActivity,
                        MainActivity::class.java
                    )
                )
                finish()
            }
        }

        binding.edtPassword.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                checkPasswordStrength(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSignup.setOnClickListener {
            saveUserInfo()
        }

        binding.btnSignin.setOnClickListener {
            val i = Intent(this, SignInActivity::class.java)
            startActivity(i)
            finish()
        }
    }


    private fun saveUserInfo() {
        try {
            val userName = binding.edtUsername.text.toString()
            val userEmail = binding.edtEmail.text.toString()
            val userPassword = binding.edtPassword.text.toString()

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            locTime = "$day-$month-$year $hour:$minute"

            if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    try {
                        // Call your suspend functions here
                        viewModel.insertUserInfo(this@SignUpActivity, userName, userEmail, userPassword, locTime)
                        val status = viewModel.getUserInfo(this@SignUpActivity, userEmail, userPassword)

                        if (status) { // Assuming getUserInfo returns true on success
                            Toast.makeText(this@SignUpActivity, "Registration successful", Toast.LENGTH_SHORT).show()

                            binding.edtUsername.text?.clear()
                            binding.edtEmail.text?.clear()
                            binding.edtPassword.text?.clear()

                            val i = Intent(this@SignUpActivity, SignInActivity::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Toast.makeText(this@SignUpActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("Error", "Database Error: ${e.message}")
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("Error", "Error: ${e.message}")
        }
    }

    private fun checkPasswordStrength(password: String) {

        // Reset colors
        binding.viewWeak.setBackgroundColor(Color.parseColor("#2B2B45"))
        binding.viewMedium.setBackgroundColor(Color.parseColor("#2B2B45"))
        binding.viewStrong.setBackgroundColor(Color.parseColor("#2B2B45"))

        // Strong Password Regex
        val strongRegex =
            Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")

        // Medium Password Regex
        val mediumRegex =
            Regex("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")

        when {

            // Weak
            password.length < 6 -> {

                binding.viewWeak.setBackgroundColor(Color.RED)

                binding.txtStrength.text = "Weak Password"
                binding.txtStrength.setTextColor(Color.RED)
            }

            // Strong → GREEN
            strongRegex.matches(password) -> {

                binding.viewWeak.setBackgroundColor(Color.RED)
                binding.viewMedium.setBackgroundColor(Color.YELLOW)
                binding.viewStrong.setBackgroundColor(Color.GREEN)

                binding.txtStrength.text = "Strong Password"
                binding.txtStrength.setTextColor(Color.GREEN)
            }

            // Medium
            mediumRegex.matches(password) -> {

                binding.viewWeak.setBackgroundColor(Color.RED)
                binding.viewMedium.setBackgroundColor(Color.YELLOW)

                binding.txtStrength.text = "Medium Password"
                binding.txtStrength.setTextColor(Color.YELLOW)
            }

            else -> {

                binding.viewWeak.setBackgroundColor(Color.RED)

                binding.txtStrength.text = "Weak Password"
                binding.txtStrength.setTextColor(Color.RED)
            }
        }
    }
}
