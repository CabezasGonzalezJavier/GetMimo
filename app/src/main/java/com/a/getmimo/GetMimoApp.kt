package com.a.getmimo

import android.app.Application

class GetMimoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}