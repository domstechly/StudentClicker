package com.example.studentclicker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentclicker.model.Industries

@Database(entities = [Industries::class], version = 13, exportSchema = false)
abstract class IndustriesDatabase : RoomDatabase() {

    abstract fun industriesDao(): IndustriesDao

    companion object {
        @Volatile
        private var INSTANCE: IndustriesDatabase? = null

        fun getDatabase(context: Context): IndustriesDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IndustriesDatabase::class.java,
                    "industries_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}