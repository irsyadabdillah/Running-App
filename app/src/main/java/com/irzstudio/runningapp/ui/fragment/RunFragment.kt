package com.irzstudio.runningapp.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.adapter.RunAdapter
import com.irzstudio.runningapp.ui.activity.MainActivity
import com.irzstudio.runningapp.ui.activity.MainViewModel
import com.irzstudio.runningapp.util.Constant
import com.irzstudio.runningapp.util.SortType
import com.irzstudio.runningapp.util.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val runAdapter: RunAdapter by lazy {
        RunAdapter()
    }
    private val viewModel: MainViewModel by viewModels()

    private var navToTracking : MainActivity? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        actionBar()
        setupRecyclerView()
        requestPermissions()
        fab.setOnClickListener{
            navToTracking?.navigationToTracking()
        }

        when (viewModel.sortType) {
            SortType.DATE -> sp_filter.setSelection(0)
            SortType.RUNNING_TIME -> sp_filter.setSelection(1)
            SortType.DISTANCE -> sp_filter.setSelection(2)
            SortType.AVG_SPEED -> sp_filter.setSelection(3)
            SortType.CALORIES_BURED -> sp_filter.setSelection(4)
        }
        viewModel.runs.observe(viewLifecycleOwner, { runs ->
            runAdapter.submitList(runs)
        })

        sp_filter.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> viewModel.sortRuns(SortType.DATE)
                    1 -> viewModel.sortRuns(SortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(SortType.DISTANCE)
                    3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortType.CALORIES_BURED)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
        }
    }

    private fun actionBar() {
        (activity as AppCompatActivity).setSupportActionBar(menu_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = "RUN"

    }

    private val itemToucHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position =viewHolder.layoutPosition
            val run = runAdapter.differ.currentList[position]
            viewModel.deleteRun(run)
            Snackbar.make(requireView(), "Successfully delete Run", Snackbar.LENGTH_LONG).apply {
                setAction("Undo"){
                    viewModel.insertRun(run)
                }
                show()
            }
        }
    }

    private fun setupRecyclerView() = rv_run.apply{
        adapter = runAdapter
        layoutManager = LinearLayoutManager(activity)
        ItemTouchHelper(itemToucHelperCallback).attachToRecyclerView(this)
    }

    private fun requestPermissions(){
        if (TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this app",
                Constant.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this app",
                Constant.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}