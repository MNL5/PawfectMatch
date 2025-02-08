package com.example.pawfectmatch

import android.app.Application
import android.content.Context

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        private var appContext: Context? = null

        val context: Context
            get() = appContext ?: throw Exception("Context not found")
    }
}