package com.example.pawfectmatch.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.pawfectmatch.data.models.InflatedComment

@Dao
interface InflatedCommentDAO {
    @Query("SELECT * FROM inflatedComments WHERE postId = :id")
    fun getByPostId(id: String): LiveData<List<InflatedComment>>
}