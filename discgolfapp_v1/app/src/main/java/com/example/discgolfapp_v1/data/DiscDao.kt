package com.example.discgolfapp_v1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DiscDao {

    @Insert
    suspend fun addDisc (disc: Disc)

    @Query("SELECT * FROM disc_table ORDER BY name ASC")
    fun readAllData(): LiveData<List<Disc>>

}