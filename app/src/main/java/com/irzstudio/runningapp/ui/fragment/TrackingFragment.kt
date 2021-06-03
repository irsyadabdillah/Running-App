package com.irzstudio.runningapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.service.TrackingService
import com.irzstudio.runningapp.ui.CancelRunDialog
import com.irzstudio.runningapp.ui.activity.MainActivity
import com.irzstudio.runningapp.ui.activity.MainViewModel
import com.irzstudio.runningapp.util.Constant
import com.irzstudio.runningapp.util.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import kotlinx.android.synthetic.main.item_run.*
import javax.inject.Inject

const val CANCEL_DIALOG_TAG = "CancelDialogTag"

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    @set:Inject
    var weight: Float = 80f
    private var map: GoogleMap? = null
    private var isTracking = false
    private var currentTimeMillis = 0L
    private var pathPoints = mutableListOf<MutableList<LatLng>>()
    private var viewModel: MainViewModel by viewModels()
    private var menu: Menu? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapViewBundle = savedInstanceState?.getBundle(Constant.MAP_VIEW_BUNDLE_KEY)
        mapview.onCreate(mapViewBundle)

        if (savedInstanceState != null) {
            val cancelRunDialog = parentFragmentManager.findFragmentByTag(CANCEL_DIALOG_TAG) as CancelRunDialog
            cancelRunDialog?.setYesListener {
                stopRun()
            }
        }

        btn_resume.setOnClickListener {
            toggleRun()
        }

        btn_finish_run.setOnClickListener {
            zoomToWholeTrack()
            endRunAndSaveToDb()
        }

        mapview.getMapAsync{
            map = it
            addAllPolylines()

        }
        subscribeToObserves()
    }

    private fun subscribeToObserves() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeMillis = it
            val formattedTime =TrackingUtility.getFormattedStopWatchTime(it, true)
            tv_time.text = formattedTime
        })
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constant.MAP_ZOOM
                )
            )

        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolygonOptions()
                .color(Constant.POLYLINE_COLOR)
                .widht(Constant.POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatlng = pathPoints.last()[pathPoints.last().size - 2]
            val last
        }
    }



}