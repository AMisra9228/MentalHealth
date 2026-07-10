package com.sample.mentalhealth.login_registration.repository

import com.sample.mentalhealth.login_registration.model.SignInRequest
import com.sample.mentalhealth.login_registration.model.SignInResponse
import com.sample.mentalhealth.retrofit.RetrofitClient

class AuthRepository {

    private val api = RetrofitClient.apiService

    suspend fun signIn(email: String, password: String): Result<SignInResponse> {
        return try {
            val request = SignInRequest(email = email.trim(), password = password)
            val response = api.signIn(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.user != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Login failed"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val message = parseErrorMessage(errorBody) ?: "Error ${response.code()}: Login failed"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Please check your connection"}"))
        }
    }

    private fun parseErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val gson = com.google.gson.Gson()
            val response = gson.fromJson(errorBody,SignInResponse::class.java)
            response.message
        } catch (e: Exception) {
            null
        }
    }
}