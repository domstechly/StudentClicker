package com.example.studentclicker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.studentclicker.data.PointsDatabase
import com.example.studentclicker.repository.PointsRepository
import com.example.studentclicker.model.Points
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PointsViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Points>>
    private val repository: PointsRepository

    init {
        val pointsDao = PointsDatabase.getDatabase(
            application
        ).pointsDao()
        repository = PointsRepository(pointsDao)
        readAllData = repository.readAllData
    }

    fun updateIndustry(points: Points){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateIndustry(points)
        }
    }
    fun addIndustry(points: Points){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addData(points)
        }
    }

}