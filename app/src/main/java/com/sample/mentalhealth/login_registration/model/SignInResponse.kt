package com.sample.mentalhealth.login_registration.model

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("user")
    val user: User?
)

data class User(
    val id: String,
    val username: String,
    val email: String,
    val contact_no: String? = null,
    val address: String? = null,
    val gender: String? = null,
    val dob: String? = null,
    val token: String
)