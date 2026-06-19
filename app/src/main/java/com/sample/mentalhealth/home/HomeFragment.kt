package com.sample.mentalhealth.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.mentalhealth.MyApp
import com.sample.mentalhealth.databinding.FragmentHomeBinding
import com.sample.mentalhealth.di.ViewModelFactory
import com.sample.mentalhealth.login_registration.UserViewModelNew
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var userViewModel: UserViewModelNew

    private lateinit var operationAdapter: OperationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as MyApp)
            .appComponent
            .inject(this)
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
        loadLoggedInUser()

        return binding.root
    }

    private fun loadLoggedInUser() {

        val sharedPreferences =
            requireActivity().getSharedPreferences(
                "UserLog",
                Context.MODE_PRIVATE
            )

        val userEmail =
            sharedPreferences.getString("user_email", null)
                ?: sharedPreferences.getString("user_name", null)

        if (userEmail.isNullOrEmpty()) {
            binding.tvGreeting.text = "Welcome,"
            binding.tvUserName.text = "User"
            return
        }

        lifecycleScope.launch {

            try {

                val userName =
                    userViewModel.getUserNameByEmail(userEmail)

                binding.tvGreeting.text = "Welcome,"
                binding.tvUserName.text =
                    if (!userName.isNullOrEmpty()) {
                        userName
                    } else {
                        "User"
                    }

            } catch (e: Exception) {
                e.printStackTrace()

                binding.tvGreeting.text = "Welcome,"
                binding.tvUserName.text = "User"
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}