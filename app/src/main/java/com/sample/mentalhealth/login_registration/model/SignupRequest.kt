package com.sample.mentalhealth.login_registration.model

data class SignupRequest(
    val full_name: String,
    val username: String,
    val email: String,
    val dob: String,
    val gender: String,
    val password: String,
    val confirm_password: String
)