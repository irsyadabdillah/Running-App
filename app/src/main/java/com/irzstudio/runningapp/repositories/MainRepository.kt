package com.irzstudio.runningapp.repositories

import com.irzstudio.runningapp.db.Run
import com.irzstudio.runningapp.db.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(

    val runDao: RunDao
) {

    suspend fun insertRun (run: Run) = runDao.insertRun(run)

    suspend fun deleteRun (run:Run) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()
    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()
    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()
    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()
    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
    fun getTotalDistance() = runDao.getTotalDistance()
    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()
    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()




}