package com.sample.mentalhealth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sample.mentalhealth.common.SessionManager
import com.sample.mentalhealth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        setUpNavBottom()

        debugSession()

    }

    private fun setUpNavBottom(){
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.hostFragment) as NavHostFragment

        navController = navHostFragment.navController

        binding.btmNav.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    // Add to MainActivity.onCreate() for debugging
    private fun debugSession() {
        val sessionManager = SessionManager(this)
        android.util.Log.d("DEBUG", "=== Session Debug ===")
        android.util.Log.d("DEBUG", "IsLoggedIn: ${sessionManager.isLoggedIn()}")
        android.util.Log.d("DEBUG", "Token: ${sessionManager.getToken()}")
        android.util.Log.d("DEBUG", "Username: ${sessionManager.getUsername()}")
        android.util.Log.d("DEBUG", "Email: ${sessionManager.getEmail()}")
        android.util.Log.d("DEBUG", "=====================")
    }
}