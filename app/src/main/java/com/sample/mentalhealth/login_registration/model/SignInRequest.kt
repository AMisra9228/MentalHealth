package com.sample.mentalhealth.login_registration.model

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("username")
    val email: String,

    @SerializedName("password")
    val password: String
)