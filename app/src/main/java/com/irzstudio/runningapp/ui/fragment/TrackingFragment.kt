package com.irzstudio.runningapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.db.Run
import com.irzstudio.runningapp.service.TrackingService
import com.irzstudio.runningapp.ui.CancelRunDialog
import com.irzstudio.runningapp.ui.activity.MainActivity
import com.irzstudio.runningapp.ui.activity.MainViewModel
import com.irzstudio.runningapp.util.Constant
import com.irzstudio.runningapp.util.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import kotlinx.android.synthetic.main.item_run.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.math.round

const val CANCEL_DIALOG_TAG = "CancelDialogTag"

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    @set:Inject
    var weight: Float = 80f
    private var map: GoogleMap? = null
    private var isTracking = false
    private var currentTimeMillis = 0L
    private var pathPoints = mutableListOf<MutableList<LatLng>>()
    private val viewModel: MainViewModel by viewModels()
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
            //addAllPolylines()

        }
        subscribeToObserves()
    }

    private fun subscribeToObserves() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            //addLatestPolyline()
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

    /*private fun addAllPolylines() {
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
            val lastLtlng = pathPoints.last().last()
            val polylineOptions = PolygonOptions()
                .color(Constant.POLYLINE_COLOR)
                .widht(Constant.POLYLINE_WIDTH)
                .addAll(preLastLatlng)
                .addAll(lastLtlng)
            map?.addPolyline(polylineOptions)
        }
    }*/

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking && currentTimeMillis > 0L) {
            btn_toggle_run.text = "Start"
            btn_finish_run.visibility = View.VISIBLE
        }else if (isTracking){
            btn_toggle_run.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            btn_finish_run.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    private fun toggleRun() {
        if (isTracking) {
            menu?.getItem(0)?.isVisible = true
            pauseTrackingService()
        }else{
            startOrResumeTrackingService()
            Timber.d("Started service")
        }
    }

    private fun startOrResumeTrackingService() =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = Constant.ACTION_START_OR_RESUME_SERVICE
            requireContext().startService(it)
        }

    private fun pauseTrackingService() =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = Constant.ACTION_PAUSE_SERVICE
            requireContext().startService(it)
        }

    private fun stopTrackingService() =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = Constant.ACTION_STOP_SERVICE
            requireContext().startService(it)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        val mapViewBundle = outState.getBundle(Constant.MAP_VIEW_BUNDLE_KEY)
        mapview?.onSaveInstanceState(mapViewBundle)
    }

    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            for (point in polyline) {
                bounds.include(point)
            }
        }

        val width = mapview.width
        val height = mapview.height
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width,
                height,
                (height * 0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (currentTimeMillis / 1000f / 60 / 60) * 10 ) / 10f
            val timeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val run = Run(bmp, timeStamp, avgSpeed, distanceInMeters, currentTimeMillis, caloriesBurned)
            viewModel.insertRun(run)

            /*Snackbar.make(requireActivity().findViewById(R.id.rootView),
            "Run saved successfully", Snackbar.LENGTH_SHORT).show()*/
            stopRun()
        }
    }

    private fun stopRun() {
        Timber.d("STOPPING RUN")
        tv_timer.text = "00:00:00:00"
        stopTrackingService()
        startActivity(Intent(activity, RunFragment::class.java))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        if (currentTimeMillis > 0L) {
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    private fun showCancelTrackingDialog() {
        CancelRunDialog().apply {
            setYesListener {
                stopRun()
            }
        }.show(parentFragmentManager, CANCEL_DIALOG_TAG)
    }

    override fun onResume() {
        super.onResume()
        mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapview.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapview.onLowMemory()
    }



}