package com.example.pawfectmatch.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pawfectmatch.data.models.PawPrint

@Dao
interface PawPrintDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg pawPrint: PawPrint)

    @Query("SELECT * FROM `paw-prints` WHERE postId = :postId AND userId = :userId")
    fun getByPostIdAndUserId(postId: String, userId: String): LiveData<PawPrint>

    @Query("DELETE FROM `paw-prints` WHERE id = :pawPrintId")
    fun delete(pawPrintId: String)

    @Query("DELETE FROM `paw-prints` WHERE postId = :postId AND userId = :userId")
    fun deleteByPostIdAndUserId(postId: String, userId: String)
}