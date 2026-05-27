package com.sample.mentalhealth.category

import androidx.lifecycle.ViewModel
import com.sample.mentalhealth.data.entities.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepo) : ViewModel() {
    fun insertData(itemName: String, itemDes: String,itemPriority: String, itemDt: String)  {
        val item = Item(itemName, itemDes,itemPriority,itemDt,"N")

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(item)
        }
    }
}