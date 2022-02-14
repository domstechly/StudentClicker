package com.example.studentclicker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentclicker.model.Points

@Dao
interface PointsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(points: Points)

    @Update
    suspend fun updateIndustry(points: Points)

    @Query("SELECT * FROM points_table ORDER BY id ASC")
    fun readAllData():LiveData<List<Points>>

}