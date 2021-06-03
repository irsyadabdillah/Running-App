package com.irzstudio.runningapp

import android.app.Application
import android.content.Context

class AppController: Application() {

    companion object{
        private var INSTANCE: AppController? = null

        @JvmStatic
        fun getInstance() : Context {
            return INSTANCE as AppController
        }

    }
}