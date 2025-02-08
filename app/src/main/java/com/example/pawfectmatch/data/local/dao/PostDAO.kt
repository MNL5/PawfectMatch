package com.example.pawfectmatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pawfectmatch.data.models.Post

@Dao
interface PostDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg review: Post)

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getById(postId: String): Post?

    @Query("SELECT * FROM posts WHERE animalId = :animalId AND userId = :userId")
    fun getByRestaurantIdAndUserId(animalId: String, userId: String): Post?

    @Query("DELETE FROM posts WHERE id = :postId")
    fun delete(postId: String)
}