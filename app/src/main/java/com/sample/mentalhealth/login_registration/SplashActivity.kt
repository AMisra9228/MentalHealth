package com.sample.mentalhealth.login_registration

//import com.google.firebase.messaging.FirebaseMessaging
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.sample.mentalhealth.MainActivity
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.databinding.ActivitySplashNewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashNewBinding
    private val splashDelay: Long = 2500
    private lateinit var viewModel: UserViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        // 1. Initialize Firebase BEFORE anything else
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)

        val savedName = sharedPreferences.getString("user_name", "")
        val savedPwd = sharedPreferences.getString("pwd", "")

        binding = ActivitySplashNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Setup Lottie
        val lottie = binding.lottieAnimation
        lottie.setAnimation("meditation.json")
        lottie.playAnimation()

        lifecycleScope.launch {

            val user = viewModel.getRegisteredUser(this@SplashActivity)

            if (user != null && (!savedName.equals("") && !savedPwd.equals(""))) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                }, splashDelay)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            SignUpActivity::class.java
                        )
                    )
                    finish()
                }, splashDelay)
            }
        }
    }
}