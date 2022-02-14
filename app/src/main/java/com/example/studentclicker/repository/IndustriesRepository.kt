package com.example.studentclicker.repository

import androidx.lifecycle.LiveData
import com.example.studentclicker.data.IndustriesDao
import com.example.studentclicker.model.Industries

class IndustriesRepository(private val industriesDao: IndustriesDao) {

    val readAllData: LiveData<List<Industries>> = industriesDao.readAllData()

    suspend fun updateIndustry(industries: Industries){
        industriesDao.updateIndustry(industries)
    }
    suspend fun selectIndustry(current_industry:Int){
        industriesDao.readIndustry(current_industry)
    }
    suspend fun addData(industries: Industries){
        industriesDao.addData(industries)
    }
}