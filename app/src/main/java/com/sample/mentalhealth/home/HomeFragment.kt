package com.sample.mentalhealth.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.mentalhealth.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var operationAdapter: OperationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupGreeting()
        setupOperations()

        return binding.root
    }

    private fun setupGreeting() {

        // Replace with actual logged-in username
        val userName = "John Doe"

        binding.tvGreeting.text = "Welcome,"
        binding.tvUserName.text = userName
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
                    // Navigate to Mood Tracker screen
                }

                "Journal" -> {
                    // Navigate to Journal screen
                }

                "Meditation" -> {
                    // Navigate to Meditation screen
                }

                "Assessment" -> {
                    // Navigate to Assessment screen
                }

                "Reports" -> {
                    // Navigate to Reports screen
                }
            }
        }

        binding.rvOperations.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            adapter = operationAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}