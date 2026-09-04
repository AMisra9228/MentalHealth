package com.sample.mentalhealth.login_registration.repository

import android.util.Log
import com.sample.mentalhealth.login_registration.model.SignupRequest
import com.sample.mentalhealth.login_registration.model.SignupResponse
import com.sample.mentalhealth.retrofit.RetrofitClient

class SignupRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun signup(request: SignupRequest): Result<SignupResponse> {
        return try {
            val response = apiService.signup(request)
            if (response.isSuccessful) {
                Log.d("SIGNUP", response.body().toString()?: "Empty")
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response from server"))
                }
            } else {
                Log.d("SIGNUP_ERROR", response.errorBody()?.string() ?: "No error body")
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Server error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}