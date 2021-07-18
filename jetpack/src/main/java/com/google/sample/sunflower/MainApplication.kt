package com.google.sample.sunflower

import android.app.Application

class MainApplication : Application() {

    companion object {
        lateinit var INSTANCE: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}