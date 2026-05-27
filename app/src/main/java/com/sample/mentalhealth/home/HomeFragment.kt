package com.sample.mentalhealth.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.mentalhealth.R
import com.sample.mentalhealth.common.Utility
import com.sample.mentalhealth.data.entities.Item
import com.sample.mentalhealth.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: ItemViewModel
    lateinit var adapter: ItemAdapter
    lateinit var status: String
    lateinit var duration: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.mnuReport -> {
                    Toast.makeText(context, "Report", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.mnuLogout -> {

                    Utility().clearUserLoginData(requireContext())

                    /*Toast.makeText(context, "Successfully logout", Toast.LENGTH_SHORT).show()
                    activity?.let {
                        val intent = Intent(it, SignUpActivity::class.java)
                        it.startActivity(intent)
                        it.finish()
                    }*/
                    true
                }

                else -> false
            }
        }

        // Setup RecyclerView
        adapter = ItemAdapter(
            onItemClick = { item ->
                val jStatus = taskViewModel.jobStat(item.title)
                if (jStatus.equals("Y")) {
                    Toast.makeText(context, "Task is already completed", Toast.LENGTH_SHORT).show()
                } else {
                    showUpdateDialog(item)
                }
            },
            onItemDelete = { item ->
                taskViewModel.deleteToDo(item)
            }
        )
        binding.productsRv.layoutManager = LinearLayoutManager(context)
        binding.productsRv.adapter = adapter

        // Get the ViewModel
        taskViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        // Observe data from ViewModel
        taskViewModel.allTodo.observe(viewLifecycleOwner, Observer { todos ->
            todos?.let { adapter.submitList(it) }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showUpdateDialog(todo: Item) {
        // Inflate the dialog view
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_todo, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.editTextDes)
        val radioButtonLow: RadioButton = dialogView.findViewById(R.id.radioButton1)
        val radioButtonMedium: RadioButton = dialogView.findViewById(R.id.radioButton2)
        val radioButtonHigh: RadioButton = dialogView.findViewById(R.id.radioButton3)
        var jobDuration : CheckBox = dialogView.findViewById(R.id.taskDuration)

        if (todo.priority.equals("L")) {
            radioButtonLow.isChecked = true
            radioButtonMedium.isChecked = false
            radioButtonHigh.isChecked = false
            status = "L"
        } else if (todo.priority.equals("M")) {
            radioButtonMedium.isChecked = true
            radioButtonLow.isChecked = false
            radioButtonHigh.isChecked = false
            status = "M"
        } else {
            radioButtonHigh.isChecked = true
            radioButtonLow.isChecked = false
            radioButtonMedium.isChecked = false
            status = "H"
        }

        dialogView.findViewById<RadioGroup>(R.id.radioGroup1).setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.radioButton1) {
                    //Toast.makeText(context, "Low", Toast.LENGTH_SHORT).show()
                    status = "L"
                } else if (checkedId == R.id.radioButton2) {
                    //Toast.makeText(context, "Medium", Toast.LENGTH_SHORT).show()
                    status = "M"
                } else {
                    //Toast.makeText(context, "High", Toast.LENGTH_SHORT).show()
                    status = "H"
                }

            })

        titleEditText.setText(todo.title)
        descriptionEditText.setText(todo.description)

        jobDuration.setOnClickListener(View.OnClickListener {
            if (jobDuration.isChecked) {
                duration = "Y"
            } else {
                duration = "N"
            }
        })

        /*if(jobDuration.isChecked) {
            duration = "Y"
        } else {
            duration = "N"
        }*/


        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Update Todo")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedTitle = titleEditText.text.toString()
                val updatedTodo = todo.copy(
                    title = updatedTitle,
                    description = descriptionEditText.text.toString(),
                    priority = status,
                    jobStatus = duration
                )
                taskViewModel.updateToDo(updatedTodo) // Call ViewModel to update the task
            }
            .setNegativeButton("Cancel", null)

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}