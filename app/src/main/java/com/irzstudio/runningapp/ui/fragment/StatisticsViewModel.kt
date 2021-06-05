package com.irzstudio.runningapp.ui.fragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.irzstudio.runningapp.repositories.MainRepository

class StatisticsViewModel @ViewModelInject constructor(
    mainRepository: MainRepository
): ViewModel() {

    var totalDistance = mainRepository.getTotalDistance()
    var totalTimeMillis = mainRepository.getTotalTimeInMillis()
    var totalAvgSpeed = mainRepository.getTotalAvgSpeed()
    var totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()

    var runsSortedByDate = mainRepository.getAllRunsSortedByDate()


}
