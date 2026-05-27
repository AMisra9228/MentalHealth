package com.sample.mentalhealth.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.mentalhealth.data.entities.Info
import com.sample.mentalhealth.login_registration.User

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(info: Info)

    @Delete
    suspend fun delInfo(info: Info)

    @Query("SELECT * FROM info_table")
    fun getAllInfo(): LiveData<List<Info>>

    @Query("SELECT * FROM user_table LIMIT 1") // Assuming one user logs in
    fun getUser(): LiveData<User>

}