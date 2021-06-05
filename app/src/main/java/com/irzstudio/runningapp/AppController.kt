package com.irzstudio.runningapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class AppController: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    companion object{
        private var INSTANCE: AppController? = null

        @JvmStatic
        fun getInstance() : Context {
            return INSTANCE as AppController
        }

    }
}