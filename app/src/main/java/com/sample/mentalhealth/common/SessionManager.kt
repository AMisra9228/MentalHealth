package com.sample.mentalhealth.common

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("MedixPrefs", Context.MODE_PRIVATE)

    fun saveUserSession(
        token: String,
        userId: String,
        username: String,
        email: String,
        rememberMe: Boolean
    ) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            putBoolean(KEY_REMEMBER_ME, rememberMe)
            apply()
        }
    }

    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)
    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)
    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getRememberedEmail(): String? = prefs.getString(KEY_REMEMBER_EMAIL, null)

    fun saveRememberedEmail(email: String, remember: Boolean) {
        prefs.edit().apply {
            if (remember) putString(KEY_REMEMBER_EMAIL, email)
            else remove(KEY_REMEMBER_EMAIL)
            apply()
        }
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_REMEMBER_EMAIL = "remember_email"
    }
}