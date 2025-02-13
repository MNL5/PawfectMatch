package com.example.pawfectmatch.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.pawfectmatch.data.models.InflatedPost

@Dao
interface InflatedPostDAO {
    @Query("SELECT * FROM inflatedPosts WHERE userId != :loggedUserId ")
    fun getAll(loggedUserId: String): LiveData<List<InflatedPost>>

    @Query("SELECT * FROM inflatedPosts WHERE userId = :id")
    fun getByUserId(id: String): LiveData<List<InflatedPost>>
}