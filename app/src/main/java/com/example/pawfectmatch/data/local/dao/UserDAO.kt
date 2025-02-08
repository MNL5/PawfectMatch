package com.example.pawfectmatch.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pawfectmatch.data.models.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getById(userId: String): User?

    @Query("SELECT * FROM users " +
            "WHERE username LIKE '%' || :searchString || '%' " +
            "ORDER BY lastUpdated DESC")
    fun getByIncluding(searchString: String): LiveData<List<User>>
}