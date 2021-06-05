package com.irzstudio.runningapp.ui

import android.annotation.SuppressLint
import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.irzstudio.runningapp.db.Run
import com.irzstudio.runningapp.util.TrackingUtility
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import kotlinx.android.synthetic.main.item_run.view.*
import kotlinx.android.synthetic.main.item_run.view.tv_date
import kotlinx.android.synthetic.main.item_run.view.tv_distance
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ViewConstructor")

class CustomMarkerView(
    val runs: List<Run>, c: Context, layoutId: Int
) : MarkerView(c, layoutId) {

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e == null) {
            return
        }
        val currentRunId = e.x.toInt()
        val run = runs[currentRunId]
        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp.toLong()
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        tv_date.text = dateFormat.format(calendar.time)

        "${run.avgSpeedInKMH} Km/h".also {
            tv_average_speed.text = it
        }
        "${run.distanceInMeters} Km/h".also {
            tv_distance.text = it
        }
        "${run.caloriesBurned} Km/h".also {
            tv_calories.text = it
        }
        tv_duration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }
}


