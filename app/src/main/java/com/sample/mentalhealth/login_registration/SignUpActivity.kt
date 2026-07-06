package com.sample.mentalhealth.login_registration

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sample.mentalhealth.MainActivity
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.R
import com.sample.mentalhealth.common.PasswordStrength
import com.sample.mentalhealth.databinding.ActvySignupBinding
import com.sample.mentalhealth.login_registration.viewmodel.SignupState
import com.sample.mentalhealth.login_registration.viewmodel.SignupViewModel
import com.sample.mentalhealth.mvpdemo.contract.LoginContract
import com.sample.mentalhealth.mvpdemo.view.LoginActivity
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.jvm.java

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActvySignupBinding
    private val viewModel: SignupViewModel by viewModels()

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActvySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        // --- Signup State ---
        viewModel.signupState.observe(this) { state ->
            when (state) {
                is SignupState.Idle -> {
                    binding.btnSignup.isEnabled = true
                    binding.btnSignup.text = "Create Account"
                }
                is SignupState.Loading -> {
                    binding.btnSignup.isEnabled = false
                    binding.btnSignup.text = "Creating Account..."
                }
                is SignupState.Success -> {
                    binding.btnSignup.isEnabled = true
                    binding.btnSignup.text = "Create Account"
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    // Navigate to login or home screen
                    // startActivity(Intent(this, LoginActivity::class.java))
                    //finish()
                }
                is SignupState.Error -> {
                    binding.btnSignup.isEnabled = true
                    binding.btnSignup.text = "Create Account"
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        }

        // --- Field Errors ---
        viewModel.fullNameError.observe(this) { error ->
            binding.edtName.error = error
        }

        viewModel.usernameError.observe(this) { error ->
            binding.edtUsername.error = error
        }

        viewModel.emailError.observe(this) { error ->
            binding.edtEmail.error = error
        }

        viewModel.dobError.observe(this) { error ->
            if (error != null) {
                binding.edtDob.error = error
                binding.edtDob.requestFocus()
            }
        }

        viewModel.genderError.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.passwordError.observe(this) { error ->
            binding.edtPassword.error = error
        }

        viewModel.confirmPasswordError.observe(this) { error ->
            binding.edtConfirmPassword.error = error
        }

        viewModel.termsError.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        // --- Password Strength ---
        viewModel.passwordStrength.observe(this) { strength ->
            updatePasswordStrengthUI(strength)
        }
    }

    private fun setupListeners() {

        // Password strength real-time
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updatePasswordStrength(s.toString())
            }
        })

        // Date of birth — show DatePicker
        binding.edtDob.setOnClickListener {
            showDatePicker()
        }

        // Create Account
        binding.btnSignup.setOnClickListener {
            hideKeyboard()

            val fullName = binding.edtName.text.toString()
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val dob = binding.edtDob.text.toString()
            val genderId = binding.rgGender.checkedRadioButtonId
            val genderText = if (genderId != -1) {
                findViewById<RadioButton>(genderId).text.toString()
            } else ""

            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            val termsChecked = binding.checkTerms.isChecked

            viewModel.validateAndSignup(
                fullName = fullName,
                username = username,
                email = email,
                dob = dob,
                genderId = genderId,
                genderText = genderText,
                password = password,
                confirmPassword = confirmPassword,
                termsChecked = termsChecked
            )
        }

        // Sign In
        binding.btnSignin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Google Sign-In
        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Google sign-in not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format as YYYY-MM-DD
                val formatted = String.format(
                    "%04d-%02d-%02d",
                    selectedYear, selectedMonth + 1, selectedDay
                )
                binding.edtDob.setText(formatted)
            },
            year, month, day
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    private fun updatePasswordStrengthUI(strength: PasswordStrength) {
        binding.txtStrength.text = strength.label

        val colorWeak = Color.parseColor("#2B2B45")
        val colorRed = Color.parseColor("#FF3B30")
        val colorOrange = Color.parseColor("#FF9500")
        val colorGreen = Color.parseColor("#34C759")

        when (strength) {
            PasswordStrength.NONE -> {
                binding.viewWeak.setBackgroundColor(colorWeak)
                binding.viewMedium.setBackgroundColor(colorWeak)
                binding.viewStrong.setBackgroundColor(colorWeak)
            }
            PasswordStrength.WEAK -> {
                binding.viewWeak.setBackgroundColor(colorRed)
                binding.viewMedium.setBackgroundColor(colorWeak)
                binding.viewStrong.setBackgroundColor(colorWeak)
                binding.txtStrength.setTextColor(colorRed)
            }
            PasswordStrength.MEDIUM -> {
                binding.viewWeak.setBackgroundColor(colorOrange)
                binding.viewMedium.setBackgroundColor(colorOrange)
                binding.viewStrong.setBackgroundColor(colorWeak)
                binding.txtStrength.setTextColor(colorOrange)
            }
            PasswordStrength.STRONG -> {
                binding.viewWeak.setBackgroundColor(colorGreen)
                binding.viewMedium.setBackgroundColor(colorGreen)
                binding.viewStrong.setBackgroundColor(colorGreen)
                binding.txtStrength.setTextColor(colorGreen)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.btnSignup.windowToken, 0)
    }
}
