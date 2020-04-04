package com.why.bluetoothtouchpad2

import android.content.Context
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.MouseSender

object Main {
    lateinit var c:Context
    var mouse: MouseSender?=null

    fun init(co:Context){
        c=co
        BluetoothController.init(c)
        println("hihihihi")

    }
}