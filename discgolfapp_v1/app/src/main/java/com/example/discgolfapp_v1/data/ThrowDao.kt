package com.example.discgolfapp_v1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ThrowDao {
    @Insert
    suspend fun addThrow (discThrow: Throw)

    @Query("SELECT * FROM throw_table ORDER BY distance DESC")
    fun readAllData(): LiveData<List<Throw>>
}