package com.sample.mentalhealth.login_registration.repository

import com.sample.mentalhealth.login_registration.model.ForgotPasswordRequest
import com.sample.mentalhealth.retrofit.ApiService
import com.sample.mentalhealth.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ForgotPasswordRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    suspend fun sendResetLink(emailOrUsername: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = ForgotPasswordRequest(emailOrUsername = emailOrUsername)
                val response = apiService.forgotPassword(request)

                if (response.status == true) {
                    Result.success(response.message ?: "Reset link sent successfully")
                } else {
                    Result.failure(Exception(response.message ?: response.message ?: "Something went wrong"))
                }
            } catch (e: HttpException) {
                Result.failure(Exception("Server error: ${e.code()}"))
            } catch (e: Exception) {
                Result.failure(Exception(e.message ?: "Network error. Please try again."))
            }
        }
    }
}