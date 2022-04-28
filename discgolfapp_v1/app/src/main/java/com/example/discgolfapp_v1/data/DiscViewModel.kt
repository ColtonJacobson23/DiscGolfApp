package com.example.discgolfapp_v1.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscViewModel (application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Disc>>
    private val repository: DiscRepository


    init
    {
        val discDao = DiscDatabase.getDatabase(application).discDao()
        repository = DiscRepository(discDao)
        readAllData = repository.readALlData
    }

    fun addDisc (disc:Disc)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.addDisc(disc)
        }
    }
}