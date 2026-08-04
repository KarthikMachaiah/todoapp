package com.example.todoapp

import android.app.Application
import com.airbnb.mvrx.Mavericks

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Airbnb Mavericks (MvRx) state management framework
        Mavericks.initialize(this)
    }
}
