package com.sample.mentalhealth.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sample.mentalhealth.data.entities.Item

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(item: Item)

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * FROM item_table ORDER BY id ASC")
    fun getAllTasks(): LiveData<List<Item>>

    @Query("SELECT jobStatus FROM item_table WHERE title = :title")
    suspend fun getJobStatus(title: String): String

    @Delete
    suspend fun delete(item: Item)
}