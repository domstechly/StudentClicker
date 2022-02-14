package com.example.studentclicker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentclicker.model.Industries

@Dao
interface IndustriesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(industries: Industries)

    @Update
    suspend fun updateIndustry(industries: Industries)

    @Query("SELECT * FROM industries_table ORDER BY id ASC")
    fun readAllData():LiveData<List<Industries>>

    @Query("SELECT * FROM industries_table WHERE id=:current_industry")
    fun readIndustry(current_industry:Int):LiveData<List<Industries>>
}