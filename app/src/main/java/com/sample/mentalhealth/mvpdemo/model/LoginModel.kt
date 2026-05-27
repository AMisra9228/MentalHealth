package com.sample.mentalhealth.mvpdemo.model

import com.sample.mentalhealth.mvpdemo.contract.LoginContract

class LoginModel : LoginContract.Model {
    override fun login(username: String, password: String, callback: (Boolean, String?) -> Unit) {
        // Fake validation (replace with API or DB later)
        if (username == "admin" && password == "1234") {
            callback(true, null)
        } else {
            callback(false, "Invalid credentials")
        }
    }
}