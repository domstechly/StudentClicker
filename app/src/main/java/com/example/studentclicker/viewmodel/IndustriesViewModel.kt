package com.example.studentclicker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.studentclicker.data.IndustriesDatabase
import com.example.studentclicker.repository.IndustriesRepository
import com.example.studentclicker.model.Industries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IndustriesViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Industries>>
    private val repository: IndustriesRepository

    init {
        val industriesDao = IndustriesDatabase.getDatabase(
            application
        ).industriesDao()
        repository = IndustriesRepository(industriesDao)
        readAllData = repository.readAllData
    }

    fun updateIndustry(industries: Industries){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateIndustry(industries)
        }
    }
    fun selectIndustry(current_industry:Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.selectIndustry(current_industry)
        }
    }
    fun addIndustry(industries: Industries){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addData(industries)
        }
    }

}