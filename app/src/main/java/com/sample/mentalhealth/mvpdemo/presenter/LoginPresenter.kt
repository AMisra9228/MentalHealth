package com.sample.mentalhealth.mvpdemo.presenter

import com.sample.mentalhealth.mvpdemo.contract.LoginContract

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginContract.Model
) : LoginContract.Presenter {

    override fun login(username: String, password: String) {
        view.showProgress()
        model.login(username, password) { success, error ->
            view.hideProgress()
            if (success) {
                view.showLoginSuccess()
            } else {
                view.showLoginError(error ?: "Unknown error")
            }
        }
    }
}