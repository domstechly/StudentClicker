package com.example.studentclicker.repository

import androidx.lifecycle.LiveData
import com.example.studentclicker.data.PointsDao
import com.example.studentclicker.model.Points

class PointsRepository(private val pointsDao: PointsDao) {

    val readAllData: LiveData<List<Points>> = pointsDao.readAllData()

    suspend fun updateIndustry(points: Points){
        pointsDao.updateIndustry(points)
    }
    suspend fun addData(points: Points){
        pointsDao.addData(points)
    }
}