package com.irzstudio.runningapp.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.irzstudio.runningapp.AppController
import com.irzstudio.runningapp.util.Constant

@Database(entities = [Run::class], version = 1)
@TypeConverters(Converters::class)
abstract class RunDatabase: RoomDatabase() {

    abstract fun getRunDao(): RunDao

    companion object {
        private var INSTANCE: RunDatabase? = null

        fun getInstance(): RunDatabase? {
            if (INSTANCE == null) {
                synchronized(RunDatabase::class) {
                    INSTANCE = Room.databaseBuilder(AppController.getInstance().applicationContext,
                        RunDatabase::class.java, Constant.DATABASE_NAME).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}


