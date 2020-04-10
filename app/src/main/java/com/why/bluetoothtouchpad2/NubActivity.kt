package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.why.bluetoothtouchpad2.Main.mouse
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.Sender
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class NubActivity : Activity() {
    var m: Sender? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nub)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    }

    fun moveLeft(view: View) {
        Main.mp?.start()
        startActivity(Intent(this, KeyboardActivity::class.java))
    }

    fun moveRight(view: View) {
        Main.mp?.start()
        startActivity(Intent(this, MouseActivity::class.java))

    }
    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()





        BluetoothController.getSender { hid, device ->
            println("nuub")
            val mainHandler = Handler(this.mainLooper)
            mainHandler.post {


                m = Sender(mouse!!, hid, device)
                println("hihihinub")
                mouse?.sendScroll(0, 0)

                findViewById<JoystickView>(R.id.joystickView)
                    .setOnMoveListener({ a, s ->
                        val x = (s / 15) * cos(Math.toRadians(a.toDouble()))
                        val y = -((s / 15) * sin(Math.toRadians(a.toDouble())))
                        var yInt = y.roundToInt()
                        if (yInt > 2047) yInt = 2047
                        if (yInt < -2047) yInt = -2047
                        var xInt = x.roundToInt()
                        if (xInt > 2047) xInt = 2047
                        if (xInt < -2047) xInt = -2047

                        println("x:" + x.toString())
                        println("y" + y)
                        println("a" + a)
                        println("s" + s)
                        mouse?.sendMouseMove(xInt, yInt)
                    }, 10)


            }

        }

        BluetoothController.getDisconnector {
            val mainHandler = Handler(this.mainLooper)

            mainHandler.post { println("byebyebye") }
        }

        findViewById<Button>(R.id.button).setOnTouchListener { v, m ->
            Main.mp?.start()
            when (m.action) {
                MotionEvent.ACTION_DOWN -> {
                    mouse?.sendLeftClickOn()
                }
                MotionEvent.ACTION_UP -> {
                    mouse?.sendLeftClickOff()
                }
            }
            return@setOnTouchListener true
        }
        findViewById<Button>(R.id.button2).setOnTouchListener { v, m ->
            Main.mp?.start()
            when (m.action) {
                MotionEvent.ACTION_DOWN -> {
                    mouse?.sendRightClickOn()
                }
                MotionEvent.ACTION_UP -> {
                    mouse?.sendRightClickOff()
                }
            }
            return@setOnTouchListener true
        }


    }

    public override fun onStop() {
        super.onStop()
        if (!Main.isAppRunning(applicationContext, "com.why.bluetoothtouchpad2")) {
            BluetoothController.btHid?.unregisterApp()

            BluetoothController.hostDevice = null
            BluetoothController.btHid = null
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

}
