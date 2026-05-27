package com.sample.mentalhealth.mvpdemo.contract

interface LoginContract {
    interface View {
        fun showProgress()
        fun hideProgress()
        fun showLoginSuccess()
        fun showLoginError(message: String)
    }

    interface Presenter {
        fun login(username: String, password: String)
    }

    interface Model {
        fun login(username: String, password: String, callback: (Boolean, String?) -> Unit)
    }
}