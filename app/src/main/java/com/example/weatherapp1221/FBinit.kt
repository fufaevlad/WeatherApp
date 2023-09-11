package com.example.weatherapp1221

import android.app.Application
import com.google.firebase.FirebaseApp

class FBInit : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}