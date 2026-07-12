package com.sample.mentalhealth.login_registration.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    @SerializedName("email_or_username")
    val emailOrUsername: String
)