package com.why.bluetoothtouchpad2

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController

class ArchLifecycleApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        BluetoothController.btHid?.unregisterApp()

        BluetoothController.hostDevice = null
        BluetoothController.btHid = null
        println("goodnight")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        // App in foreground
    }

}