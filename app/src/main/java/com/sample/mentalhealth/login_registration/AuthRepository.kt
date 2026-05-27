package com.sample.mentalhealth.login_registration

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun sendPasswordResetEmail(email: String): Result<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success("Password reset email sent.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}