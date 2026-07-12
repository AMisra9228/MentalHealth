package com.sample.mentalhealth.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.common.SessionManager
import com.sample.mentalhealth.databinding.FragmentHomeBinding
import com.sample.mentalhealth.di.ViewModelFactory
import com.sample.mentalhealth.login_registration.UserViewModelNew
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var userViewModel: UserViewModelNew
    private lateinit var sessionManager: SessionManager  // ✅ Added SessionManager

    private lateinit var operationAdapter: OperationAdapter
    private lateinit var overviewAdapter: OverviewAdapter
    private lateinit var updatesAdapter: UpcomingUpdatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as MyApp)
            .appComponent
            .inject(this)

        // ✅ Initialize SessionManager
        sessionManager = SessionManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        userViewModel =
            ViewModelProvider(this, viewModelFactory)[UserViewModelNew::class.java]

        setupOperations()
        setupUpcomingUpdates()
        setupOverviewRecycler()  // ✅ Added this (was missing)
        loadLoggedInUser()

        return binding.root
    }

    // ✅ Updated to use SessionManager
    private fun loadLoggedInUser() {

        // ✅ Get username directly from SessionManager (stored after login)
        val username = sessionManager.getUsername()
        val email = sessionManager.getEmail()
        val token = sessionManager.getToken()  // Available if needed for API calls

        if (username.isNullOrEmpty()) {
            binding.tvGreeting.text = "Welcome,"
            binding.tvUserName.text = "User"
            return
        }

        // ✅ Directly use the stored username - no API call needed!
        binding.tvGreeting.text = "Welcome,"
        binding.tvUserName.text = username

        // Optional: Log for debugging
        println("HomeFragment - Token: ${token?.take(20)}...")
        println("HomeFragment - Username: $username")
        println("HomeFragment - Email: $email")
    }

    private fun setupOperations() {

        val operations = listOf(
            Operation("Mood Tracker", android.R.drawable.ic_menu_edit),
            Operation("Journal", android.R.drawable.ic_menu_agenda),
            Operation("Meditation", android.R.drawable.ic_media_play),
            Operation("Assessment", android.R.drawable.ic_menu_info_details),
            Operation("Reports", android.R.drawable.ic_menu_view)
        )

        operationAdapter = OperationAdapter(operations) { operation ->

            when (operation.title) {
                "Mood Tracker" -> {
                    // TODO Navigate
                }
                "Journal" -> {
                    // TODO Navigate
                }
                "Meditation" -> {
                    // TODO Navigate
                }
                "Assessment" -> {
                    // TODO Navigate
                }
                "Reports" -> {
                    // TODO Navigate
                }
            }
        }

        binding.rvOperations.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = operationAdapter
        }
    }

    private fun setupOverviewRecycler() {

        overviewAdapter = OverviewAdapter { item ->
            Toast.makeText(
                requireContext(),
                item.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvOverview.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvOverview.adapter = overviewAdapter

        loadOverviewApi()
    }

    // ✅ Updated to use token from SessionManager for API call
    private fun loadOverviewApi() {

        val token = sessionManager.getToken()

        if (token.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please login again",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
    }

    private fun setupUpcomingUpdates() {

        updatesAdapter = UpcomingUpdatesAdapter(mutableListOf()) { item ->
            Toast.makeText(
                requireContext(),
                item.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rvUpcomingUpdates.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = updatesAdapter
            setHasFixedSize(true)
        }

        val updates = listOf(
            UpcomingUpdate("AI Mood Tracker", "Track user mood automatically using AI insights."),
            UpcomingUpdate("Dark Mode Support", "Complete dark theme support will be added."),
            UpcomingUpdate("Daily Reminders", "Users can configure daily mental wellness reminders."),
            UpcomingUpdate("Report Download", "PDF report download functionality for counsellors."),
            UpcomingUpdate("Chat Support", "In-app secure chat between counsellor and patient."),
            UpcomingUpdate("Cloud Backup", "Automatic cloud backup and restore feature.")
        )

        updatesAdapter.submitList(updates)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}