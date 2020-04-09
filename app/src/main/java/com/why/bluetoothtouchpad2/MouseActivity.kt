package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.why.bluetoothtouchpad2.Main.isAppRunning
import com.why.bluetoothtouchpad2.Main.mouse
import com.why.bluetoothtouchpad2.bluetooth.*
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController.btHid
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController.hostDevice


class MouseActivity : Activity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mouse)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    }


    var m: Sender? = null

    @ExperimentalUnsignedTypes
    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        Main.init(this.applicationContext)





        BluetoothController.getSender { hid, device ->
            println("hihihihihi")
            val mainHandler = Handler(this.mainLooper)
            mainHandler.post {

                mouse =
                    MouseSender(
                        hid,
                        device
                    )

                m = Sender(mouse!!, hid, device)
                println("hihihi")

                val viewTouchListener = MouseViewListener(hid, device, mouse!!)

                class CustomGestureDetector(context: Context, private var mListenerMouse: MouseGestureDetectListener) :
                    GestureDetector(context, mListenerMouse) {

                    override fun onTouchEvent(ev: MotionEvent?): Boolean {
                        val consume = mListenerMouse.onTouchEvent(ev)
                        return consume || super.onTouchEvent(ev)
                    }


                }
                val mDetector = CustomGestureDetector(this, MouseGestureDetectListener(mouse!!,this))

                val gTouchListener = View.OnTouchListener { v, event -> mDetector.onTouchEvent(event) }
                val comp=object:View.OnTouchListener{
                    private var registeredListeners : MutableList<View.OnTouchListener> = ArrayList<View.OnTouchListener>()


                    fun registerListener(listener : View.OnTouchListener) {
                        registeredListeners.add(listener)

                    }
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                        for(listener:View.OnTouchListener in registeredListeners)
                        {
                            listener.onTouch(v,event)
                        }
                        return true

                    }
                }
                comp.registerListener(gTouchListener)
                comp.registerListener(viewTouchListener)

                findViewById<TextView>(R.id.touchpad).setOnTouchListener(comp)

            }

        }

        BluetoothController.getDisconnector {
            val mainHandler = Handler(this.mainLooper)

            mainHandler.post { println("byebyebye") }
        }

        findViewById<Button>(R.id.button).setOnTouchListener{v,m->
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
        findViewById<Button>(R.id.button2).setOnTouchListener{v,m->
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
        if (!isAppRunning(applicationContext,"com.why.bluetoothtouchpad2")) {
            btHid?.unregisterApp()

                  hostDevice = null
                btHid = null
            println("goodnight")
        }

    }

    fun moveLeft(view: View) {
        startActivity(Intent(this,NubActivity::class.java))
    }
    fun moveRight(view: View) {
        startActivity(Intent(this,KeyboardActivity::class.java))

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

    override fun onRestart() {
        super.onRestart()
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }


}
