package com.example.pawfectmatch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pawfectmatch.data.models.Comment

@Dao
interface CommentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg comment: Comment)

    @Query("DELETE FROM comments WHERE id = :commentId")
    fun delete(commentId: String)
}