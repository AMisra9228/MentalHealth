package com.sample.mentalhealth.login_registration

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.sample.mentalhealth.MainActivity
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.common.SessionManager
import com.sample.mentalhealth.databinding.ActivitySplashNewBinding
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashNewBinding
    private val splashDelay: Long = 2500

    // ✅ Use SessionManager instead of raw SharedPreferences
    private lateinit var sessionManager: SessionManager

    @Inject
    lateinit var sharedPreferences: javax.inject.Provider<SharedPreferences> // Keep if other parts need it, or remove if unused

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)

        // 1. Initialize Firebase BEFORE anything else
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)

        // ✅ Initialize SessionManager
        sessionManager = SessionManager(this)

        binding = ActivitySplashNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Lottie
        val lottie = binding.lottieAnimation
        lottie.setAnimation("meditation.json")
        lottie.playAnimation()

        // ✅ Fixed Routing Logic
        Handler(Looper.getMainLooper()).postDelayed({

            // Check the NEW session manager
            val intent = if (sessionManager.isLoggedIn()) {
                // User is logged in -> Go to Home
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                // User is NOT logged in -> Go to SignIn (NOT SignUp!)
                Intent(this@SplashActivity, SignInActivity::class.java)
            }

            // Clear the back stack so user can't press back to splash
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }, splashDelay)
    }
}