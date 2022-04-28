package com.example.discgolfapp_v1.data

import androidx.lifecycle.LiveData

class DiscRepository(private val discDao:DiscDao ) {
    val readALlData: LiveData<List<Disc>> = discDao.readAllData()

    suspend fun addDisc(disc: Disc)
    {
        discDao.addDisc(disc)
    }
}