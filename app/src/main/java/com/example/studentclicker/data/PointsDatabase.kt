package com.example.studentclicker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentclicker.model.Points

@Database(entities = [Points::class], version = 3, exportSchema = false)
abstract class PointsDatabase : RoomDatabase() {

    abstract fun pointsDao(): PointsDao

    companion object {
        @Volatile
        private var INSTANCE: PointsDatabase? = null

        fun getDatabase(context: Context): PointsDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PointsDatabase::class.java,
                    "points_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}