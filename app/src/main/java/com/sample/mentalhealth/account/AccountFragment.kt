package com.sample.mentalhealth.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sample.mentalhealth.common.SessionManager
import com.sample.mentalhealth.databinding.FragAccBinding
import com.sample.mentalhealth.login_registration.SignInActivity

class AccountFragment : Fragment() {

    private var _binding: FragAccBinding? = null
    private val binding get() = _binding!!

    // ✅ Use SessionManager instead of Room Database
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragAccBinding.inflate(inflater, container, false)

        // ✅ Initialize SessionManager
        sessionManager = SessionManager(requireContext())

        loadUserData()
        setupClickListeners()

        return binding.root
    }

    private fun loadUserData() {
        // ✅ Fetch real data from SessionManager (Saved during API login)
        val username = sessionManager.getUsername() ?: "User"
        val email = sessionManager.getEmail() ?: "No email provided"

        binding.userName.text = username
        binding.userMail.text = email
    }

    private fun setupClickListeners() {

        // ✅ Fixed Sign Out Logic
        binding.btnSignOut.setOnClickListener {
            // 1. Clear the session
            sessionManager.clearSession()

            // 2. Navigate back to SignInActivity
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Optional: Handle Edit Profile click
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Edit Profile Screen
        }

        // Optional: Handle Update Profile text click
        binding.txtUpdateProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Update Profile Clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Edit Profile Screen
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}