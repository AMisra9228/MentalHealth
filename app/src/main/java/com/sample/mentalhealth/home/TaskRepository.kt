package com.sample.mentalhealth.home

import androidx.lifecycle.LiveData
import com.sample.mentalhealth.data.dao.ItemDao
import com.sample.mentalhealth.data.entities.Item

class TaskRepository(private val itemDao: ItemDao) {
    val allTasks: LiveData<List<Item>> = itemDao.getAllTasks()

    suspend fun update(item: Item) {
        itemDao.update(item)
    }

    suspend fun delete(item: Item) {
        itemDao.delete(item)
    }

    suspend fun jobDuration(title:String): String {
        return itemDao.getJobStatus(title)
    }
}