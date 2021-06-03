package com.irzstudio.runningapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.ui.CustomMarkerView
import com.irzstudio.runningapp.ui.activity.MainViewModel
import com.irzstudio.runningapp.util.TrackingUtility
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.item_run.*
import kotlin.math.round

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLineChart()
        subscribeToObserve()
    }

    private fun setupLineChart() {
        bar_chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            setDrawGridLines(false)
        }
        bar_chart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        bar_chart.apply {
            description.text = "Avg Speed over time"
            legend.isEnabled = false
        }
    }

    private fun subscribeToObserve() {
        viewModel.totalDistance.observe(viewLifecycleOwner, Observer{
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10) / 10f
                val totalDistanceString = "${totalDistance} km"
                tv_total_distance.text = totalDistanceString
            }
        })

        viewModel.totalTimeMillis.observe(viewLifecycleOwner, Observer{
            it?.let {
                val totalTimeMillis = TrackingUtility.getFormattedStopWatchTime(it)
                tv_total_time.text = totalTimeMillis
            }
        })

        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer{
            it?.let {
                val km = it / 1000f
                val roundedAvgSpeed = round(km * 10f) / 10f
                val totalAvgSpeed = "${roundedAvgSpeed} Km/h"
                tv_average_speed.text = totalAvgSpeed
            }
        })

        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer{
            it?.let {
                val totalCaloriesBurned = "${it} Kcal"
                tv_total_calories.text = totalCaloriesBurned
            }
        })

        viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer{
            it?.let {
                val allAvgSpeed = it.indices.map{ i -> BarEntry(i.toFloat(), it.[i].avgSpeedInKmh)}

                val bardataSet = BarDataSet(allAvgSpeed, "Avg Speed over time")
                bardataSet.apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                val lineData = BarData(bardataSet)
                bar_chart.data = lineData
                val marker = CustomMarkerView(
                    it.reversed(),
                    requireContext(),
                    R.layout.marker_view
                )
                bar_chart.marker = marker
                bar_chart.invalidate()
            }
        })
    }

}