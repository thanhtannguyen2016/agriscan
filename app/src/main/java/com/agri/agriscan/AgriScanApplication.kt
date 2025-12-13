package com.agri.agriscan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AgriScanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}