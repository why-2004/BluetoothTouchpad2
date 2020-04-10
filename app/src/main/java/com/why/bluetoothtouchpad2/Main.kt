package com.why.bluetoothtouchpad2

import android.R
import android.app.ActivityManager
import android.content.Context
import android.media.MediaPlayer
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.MouseSender


object Main {
    lateinit var c:Context
    var mouse: MouseSender?=null
    var mp:MediaPlayer?=null

    fun init(co:Context){
        c=co
        BluetoothController.init(c)
        println("hihihihi")

    }

    fun isAppRunning(
        context: Context,
        packageName: String
    ): Boolean {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos =
            activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }
}