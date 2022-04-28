package com.example.discgolfapp_v1.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThrowViewModel (application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Throw>>
    private val repository: ThrowRepository

    init
    {
        val throwDao = DiscDatabase.getDatabase(application).throwDao()
        repository = ThrowRepository(throwDao)
        readAllData = repository.readAllData
    }

    fun addThrow (discThrow: Throw)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.addThrow(discThrow)
        }
    }
}