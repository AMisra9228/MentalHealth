package com.sample.mentalhealth.category

import com.sample.mentalhealth.data.dao.ItemDao
import com.sample.mentalhealth.data.entities.Item

class CategoryRepo(private val itemDao: ItemDao)  {
    suspend fun insert(catinfo: Item) {
        itemDao.insertCategory(catinfo)
    }
}