package com.example.pawfectmatch.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.pawfectmatch.data.models.InflatedPost

@Dao
interface InflatedPostDAO {
    @Query("SELECT * FROM inflatedPosts")
    fun getAll(): LiveData<List<InflatedPost>>

    @Query("SELECT * FROM inflatedPosts WHERE userId = :userID")
    fun getByUserId(userID: String): LiveData<List<InflatedPost>>

    @Query("SELECT * FROM inflatedPosts WHERE animalId = :animalID AND userId != :loggedUserId ")
    fun getByAnimalId(animalID: String, loggedUserId: String): LiveData<List<InflatedPost>>
}