package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.Sender
import kotlin.math.roundToInt


class PointerActivity : AppCompatActivity(), SensorEventListener {
    var m: Sender? = null
    private var mSensorManager : SensorManager?= null

    //private var mGyroscope : Sensor ?= null

    private var mRotation :Sensor ?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pointer)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //mGyroscope = mSensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mRotation=mSensorManager!!.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

        mSensorManager!!.registerListener(this, this.mRotation, SensorManager.SENSOR_DELAY_FASTEST)


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
    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()





        BluetoothController.getSender { hid, device ->
            println("nuub")
            m = Sender(Main.mouse!!, hid, device)
            val mainHandler = Handler(this.mainLooper)
            mainHandler.post {





                        //Thread.sleep(10)
                    //}


            }

        }

        BluetoothController.getDisconnector {
            val mainHandler = Handler(this.mainLooper)

            mainHandler.post { println("byebyebye") }
        }

        findViewById<Button>(R.id.button).setOnTouchListener{ v, m->
            Main.mp?.start()
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    Main.mouse?.sendLeftClickOn()
                }
                MotionEvent.ACTION_UP->{
                    Main.mouse?.sendLeftClickOff()
                }
            }
            return@setOnTouchListener true
        }
        findViewById<Button>(R.id.button2).setOnTouchListener{ v, m->
            Main.mp?.start()
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    Main.mouse?.sendRightClickOn()
                }
                MotionEvent.ACTION_UP->{
                    Main.mouse?.sendRightClickOff()
                }
            }
            return@setOnTouchListener true
        }




    }

    public override fun onRestart() {
        super.onRestart()
        mSensorManager!!.registerListener(this, this.mRotation, SensorManager.SENSOR_DELAY_FASTEST)

    }

    public override fun onDestroy() {
        mSensorManager?.unregisterListener(this)
        super.onDestroy()
    }

    public override fun onStop() {
        mSensorManager?.unregisterListener(this)
        super.onStop()
        if (!Main.isAppRunning(applicationContext, "com.why.bluetoothtouchpad2")) {
            BluetoothController.btHid?.unregisterApp()

            BluetoothController.hostDevice = null
            BluetoothController.btHid = null
        }
    }

    fun moveLeft(view: View) {
        Main.mp?.start()
        startActivity(Intent(this,KeyboardActivity::class.java))

    }
    fun moveRight(view: View) {
        Main.mp?.start()
        startActivity(Intent(this,NubActivity::class.java))

    }
    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    var GyroX:Float=0.toFloat()
    var GyroY:Float=0.toFloat()
    var offsetX:Float=0.toFloat()
    var offsetY:Float=0.toFloat()
    override fun onSensorChanged(event: SensorEvent?) {
        println("sensored")
        val mySensor: Sensor = event!!.sensor
        when (mySensor.type) {
            Sensor.TYPE_GAME_ROTATION_VECTOR->{
                GyroX= event.values[2]
                GyroY= event.values[0]

                println("pointer sending")
                Main.mouse?.sendScroll(0,0)

                //while(true){
                val x=-(GyroX-offsetX)*50
                val y = -(GyroY-offsetY)*50
                var yInt=y.roundToInt()
                if (yInt > 2047) yInt = 2047
                if (yInt < -2047) yInt = -2047
                var xInt=x.roundToInt()
                if (xInt > 2047) xInt = 2047
                if (xInt < -2047) xInt = -2047

                println(GyroX)
                println(GyroY)
                println("x:"+x.toString())
                println("y"+y)
                Main.mouse?.sendMouseMove(xInt,yInt)
            }

    }
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
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

    fun recenter(view: View) {
        offsetX=GyroX
        offsetY=GyroY
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
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
        return super.onCreateView(parent, name, context, attrs)
    }
}
