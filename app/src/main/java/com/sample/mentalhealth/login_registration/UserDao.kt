package com.sample.mentalhealth.login_registration

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT status FROM  user_table WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): Boolean

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getRegisteredUser(): User?

    @Query("SELECT userName FROM user_table WHERE email = :mail LIMIT 1")
    suspend fun getUserNameByEmail(mail: String): String?
}