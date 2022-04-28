package com.example.discgolfapp_v1.data

import androidx.lifecycle.LiveData

class DiscRepository(private val discDao:DiscDao ) {
    val readALlData: LiveData<List<Disc>> = discDao.readAllData()
    val distanceDrivers: LiveData<List<Disc>> = discDao.readDataWhereDiscType(0)
    val fairwayDrivers: LiveData<List<Disc>> = discDao.readDataWhereDiscType(1)
    val midranges: LiveData<List<Disc>> = discDao.readDataWhereDiscType(2)
    val putters: LiveData<List<Disc>> = discDao.readDataWhereDiscType(3)

    suspend fun addDisc(disc: Disc)
    {
        discDao.addDisc(disc)
    }

    fun findDisc(name: String, color: Int, type: Int): LiveData<List<Disc>> {
        return discDao.findDisc(name, color, type)
    }
}