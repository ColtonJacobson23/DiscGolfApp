package com.example.discgolfapp_v1.data

import androidx.lifecycle.LiveData

class ThrowRepository (private val throwDao: ThrowDao) {
    val readAllData: LiveData<List<Throw>> = throwDao.readAllData()

    suspend fun addThrow(discThrow: Throw)
    {
        throwDao.addThrow(discThrow)
    }
}