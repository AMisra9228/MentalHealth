package com.sample.mentalhealth.mvpdemo.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.mentalhealth.R
import com.sample.mentalhealth.mvpdemo.contract.LoginContract
import com.sample.mentalhealth.mvpdemo.model.LoginModel
import com.sample.mentalhealth.mvpdemo.presenter.LoginPresenter

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter
    private lateinit var progressBar: ProgressBar
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.progressBar)
        usernameField = findViewById(R.id.etUsername)
        passwordField = findViewById(R.id.etPassword)

        presenter = LoginPresenter(this, LoginModel())

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            presenter.login(username, password)
        }
    }

    override fun showProgress() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = android.view.View.GONE
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}