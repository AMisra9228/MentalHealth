package com.sample.mentalhealth.login_registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sample.mentalhealth.MainActivity
import com.sample.mentalhealth.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.databinding.ActvySigninBinding
import com.sample.mentalhealth.di.ViewModelFactory
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {
//    private lateinit var binding: ActivitySignInBinding
//    private lateinit var viewModel: UserViewModel
    lateinit var context: Context
    var status: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: UserViewModelNew

    private lateinit var binding: ActvySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActvySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this@SignInActivity

        // Inject dependencies
        (application as MyApp).appComponent.inject(this)

        //viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
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
                    val isLoggIn: Boolean? = viewModel.getUserInfo(context, userEmail, userPassword)
                    if (isLoggIn != null) {
                        status = true

                        val sharedPreferences = getSharedPreferences("UserLog", MODE_PRIVATE)
                        sharedPreferences.edit {

                            putString("user_name", binding.atvEmailLog.text.toString())
                            putString("pwd", binding.atvPasswordLog.text.toString())
                        }

                    } else {
                        status = false
                    }
                    if (status) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        val i = Intent(context, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
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