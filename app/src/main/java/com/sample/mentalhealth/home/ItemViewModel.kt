package com.sample.mentalhealth.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sample.mentalhealth.data.TaskDatabase
import com.sample.mentalhealth.data.entities.Item
import kotlinx.coroutines.launch

class ItemViewModel(application: Application): AndroidViewModel(application) {
    var repository: TaskRepository
    var allTodo : LiveData<List<Item>>
    private val _editToDo = MutableLiveData<Item?>()

    init {
        //CoroutineScope(Dispatchers.Main).launch {
            val dao = TaskDatabase.getInstance(application).itemDao()
            repository = TaskRepository(dao)
            allTodo = repository.allTasks
        //}
    }

    fun updateToDo(item: Item) {
        viewModelScope.launch {
            repository.update(item)
        }
    }

    fun deleteToDo(item: Item) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }

    fun jobStat(jobTitle: String) : LiveData<String> {
        val result = MutableLiveData<String>()
        viewModelScope.launch {
            val returnedrepo = repository.jobDuration(jobTitle)
            result.postValue(returnedrepo)
        }
        return result
    }

    fun setEditToDo(item: Item) {
        _editToDo.value = item
    }

    fun clearEditToDo() {
        _editToDo.value = null
    }
}