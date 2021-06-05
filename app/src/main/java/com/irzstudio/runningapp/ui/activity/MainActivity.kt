package com.irzstudio.runningapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.ui.fragment.RunFragment
import com.irzstudio.runningapp.ui.fragment.SettingFragment
import com.irzstudio.runningapp.ui.fragment.StatisticsFragment
import com.irzstudio.runningapp.ui.fragment.TrackingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationToRun()
        initBottomNavigation()
    }

    private fun initBottomNavigation(){
        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.run -> {navigationToRun()}
                R.id.statistic -> {navigationToStatistic()}
                R.id.setting -> {navigationToSetting()}
            }
            true
        }
    }

    private fun navigationToRun() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, RunFragment()).commit()
    }

    private fun navigationToStatistic() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, StatisticsFragment()).commit()
    }

    private fun navigationToSetting() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, SettingFragment()).commit()
    }

    fun navigationToTracking() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, TrackingFragment()).commit()
    }

}