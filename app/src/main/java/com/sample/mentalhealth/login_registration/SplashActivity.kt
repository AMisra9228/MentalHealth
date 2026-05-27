package com.sample.mentalhealth.login_registration

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.sample.mentalhealth.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashDelay: Long = 2500

    // FIX: Declare SharedPreferences to resolve the lateinit crash
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Initialize Firebase BEFORE anything else
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)

        // 2. Initialize SharedPreferences IMMEDIATELY to prevent UninitializedPropertyAccessException
        sharedPreferences = getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Lottie
        val lottie = binding.lottieAnimation // Use binding instead of findViewById
        lottie.setAnimation("time_scedhuling.json")
        lottie.playAnimation()

        // Navigation logic
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }, splashDelay)

        // Fetch FCM Token
        fetchFcmToken()
    }

    private fun fetchFcmToken() {
        try {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // LOG THE ACTUAL ERROR HERE
                        android.util.Log.e("FCM_ERROR", "Token fetch failed", task.exception)
                        return@addOnCompleteListener
                    }

                    val token = task.result
                    android.util.Log.d("FCM_TOKEN", "Success: $token")
                    sharedPreferences.edit().putString("fcm_token", token).apply()
                }
        } catch (e: IllegalStateException) {
            // This catches the "FirebaseApp not initialized" error if it occurs at runtime
            e.printStackTrace()
        }
    }
}