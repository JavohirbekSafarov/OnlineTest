package com.javokhirbekcoder.onlinetest

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

/*
Created by Javokhirbek on 28/11/2023 at 09:52
*/

@HiltAndroidApp
class App :Application(){
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}