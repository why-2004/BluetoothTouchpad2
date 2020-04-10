package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.why.bluetoothtouchpad2.Main.mouse
import com.why.bluetoothtouchpad2.Main.mp
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.MouseSender
import com.why.bluetoothtouchpad2.bluetooth.Sender


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        mp = MediaPlayer.create(this,R.raw.click)


    }

    var m: Sender? = null

    var connected=false

    @ExperimentalUnsignedTypes
    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        Main.init(this.applicationContext)
        BluetoothController.getSender { hid, device ->
        println("startBL")
            connected=true



            val mainHandler = Handler(this.mainLooper)
        mainHandler.post {
            mouse =
                MouseSender(
                    hid,
                    device
                )
            m = Sender(mouse!!, hid, device)
            Toast.makeText(applicationContext, getString(R.string.bluetooth_connected), Toast.LENGTH_LONG).show()

        }

    }

    BluetoothController.getDisconnector {
        val mainHandler = Handler(this.mainLooper)
        mainHandler.post { println("byebyebye")
        connected=false
        }
    }


}


    fun trackpad(view: View) {
        mp?.start()
        startActivity(Intent(this,MouseActivity::class.java))

    }
    fun trackpoint(view: View) {
        mp?.start()
        startActivity(Intent(this,NubActivity::class.java))

    }
    fun pointer(view: View) {
        mp?.start()
        startActivity(Intent(this,PointerActivity::class.java))

    }
    fun keyboard(view: View) {
        mp?.start()
        startActivity(Intent(this,KeyboardActivity::class.java))

    }
    fun procontroller(view: View) {
        mp?.start()
    }

    override fun onRestart() {
        super.onRestart()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}
