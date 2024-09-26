package com.app.bmicalculator

import android.app.Application

class App : Application() {

    companion object {
        var appContext: Application? = null
    }


    override fun onCreate() {
        super.onCreate()
        appContext = this


    }


}