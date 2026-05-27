package com.sample.mentalhealth.category

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sample.mentalhealth.R
import com.sample.mentalhealth.common.Utility
import com.sample.mentalhealth.data.TaskDatabase
import com.sample.mentalhealth.databinding.FragmentCategoryBinding
import java.util.Calendar

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryViewModel: CategoryViewModel
    var taskPrio : String = ""
    var assgnDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val itemDao = TaskDatabase.getInstance(requireContext()).itemDao()
        val repository = CategoryRepo(itemDao)
        categoryViewModel = CategoryViewModelFact(repository).create(CategoryViewModel::class.java)

        binding.catToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.mnuReport -> {
                    Toast.makeText(context, "Report", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.mnuLogout -> {
                    Utility().clearUserLoginData(requireContext())
                    true
                }
                else -> false
            }
        }

        binding.radioGroup1.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                if(checkedId == R.id.radioButton1) {
                    //Toast.makeText(context, "Low", Toast.LENGTH_SHORT).show()
                    taskPrio = "L"
                } else if(checkedId == R.id.radioButton2) {
                    //Toast.makeText(context, "Medium", Toast.LENGTH_SHORT).show()
                    taskPrio = "M"
                } else {
                    //Toast.makeText(context, "High", Toast.LENGTH_SHORT).show()
                    taskPrio = "H"
                }
            })

        binding.dtSelect.setOnClickListener{
            var c = Calendar.getInstance()
            var year : Int = c.get(Calendar.YEAR)
            var month : Int = c.get(Calendar.MONTH)
            var day : Int = c.get(Calendar.DAY_OF_MONTH)

                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in textbox
                        binding.taskDt.setText("" + dayOfMonth + " - " + (Integer.valueOf(monthOfYear) + 1) + " - " + year)
                        assgnDate = binding.taskDt.text.toString()
                    }, year, month, day).show()
                }
        }

        binding.btnSave.setOnClickListener {
            saveItemInfo()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun saveItemInfo() {
        val taskName = binding.taskNameNew.text.toString()
        val taskDescription = binding.taskDespNew.text.toString()

        if(taskName.isEmpty() || taskDescription.isEmpty() || assgnDate.isEmpty()){
            Toast.makeText(context, "Enter all details", Toast.LENGTH_SHORT).show()
            return
        } else {
            Toast.makeText(context, "Task saved", Toast.LENGTH_SHORT).show()
            categoryViewModel.insertData(taskName, taskDescription, taskPrio,assgnDate)
            binding.taskNameNew.text?.clear()
            binding.taskDespNew.text?.clear()
            binding.radioGroup1.clearCheck()
            binding.taskDt!!.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}