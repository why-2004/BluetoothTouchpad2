package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.why.bluetoothtouchpad2.Main.mouse
import com.why.bluetoothtouchpad2.bluetooth.*
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
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

    }

    fun moveLeft(view: View) {
        //startActivity(Intent())
        //TODO()

    }
    fun moveRight(view: View) {
        startActivity(Intent(this,MouseActivity::class.java))

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
                mouse?.sendScroll(0,0)

                val joystick = findViewById<JoystickView>(R.id.joystickView)
                    .setOnMoveListener( {a, s->
                    val x=(s/15)*cos(Math.toRadians(a.toDouble()))
                    val y = -((s/15)*sin(Math.toRadians(a.toDouble())))
                    var yInt=y.roundToInt()
                    if (yInt > 2047) yInt = 2047
                    if (yInt < -2047) yInt = -2047
                    var xInt=x.roundToInt()
                    if (xInt > 2047) xInt = 2047
                    if (xInt < -2047) xInt = -2047

                        println("x:"+x.toString())
                        println("y"+y)
                        println("a"+a)
                        println("s"+s)
                    mouse?.sendMouseMove(xInt,yInt)
                },10)


            }

        }

        BluetoothController.getDisconnector {
            val mainHandler = Handler(this.mainLooper)

            mainHandler.post { println("byebyebye") }
        }

        findViewById<Button>(R.id.button).setOnTouchListener{ v, m->
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    mouse?.sendLeftClickOn()
                }
                MotionEvent.ACTION_UP->{
                    mouse?.sendLeftClickOff()
                }
            }
            return@setOnTouchListener true
        }
        findViewById<Button>(R.id.button2).setOnTouchListener{ v, m->
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    mouse?.sendRightClickOn()
                }
                MotionEvent.ACTION_UP->{
                    mouse?.sendRightClickOff()
                }
            }
            return@setOnTouchListener true
        }




    }
    public override fun onStop() {
        super.onStop()
        //BluetoothController.btHid?.unregisterApp()

//        BluetoothController.hostDevice = null
  //      BluetoothController.btHid = null

    }

}
