package com.sample.mentalhealth.login_registration.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("success")
    val status: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)