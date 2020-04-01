package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.app.Activity
import android.gesture.Gesture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.core.view.GestureDetectorCompat
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.MouseSender
import com.why.bluetoothtouchpad2.bluetooth.Sender

class MouseActivity : Activity(),GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    var mouse: MouseSender? = null
    private lateinit var mDetector: GestureDetectorCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mouse)
        mDetector = GestureDetectorCompat(this, this)
        mDetector.setOnDoubleTapListener(this)

    }

    fun test(view: View) {
        m?.test()
        println("hi")

    }

    var m: Sender?=null

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()


        this.findViewById<Button>(R.id.button).setOnTouchListener { _, _ ->
            println("hi6")
            m?.test()
            false
        }


        BluetoothController.init(this)
        println("hihihihi")
        BluetoothController.getSender { hid, device ->
            println("hihihihihi")
            val mainHandler = Handler(this.mainLooper)
            mainHandler.post {
                //rKeyboardSender= KeyboardSender(hid,device)

                val rMouseSender =
                    MouseSender(
                        hid,
                        device
                    )
                //val viewTouchListener = ViewListener(hid, device, rMouseSender)
                //val mDetector = CustomGestureDetector(this, GestureDetectListener(rMouseSender))

                /*val gTouchListener = object : View.OnTouchListener {

                                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                                            return mDetector.onTouchEvent(event)

                                        }

                                    }*/


                //val composite : CompositeListener = CompositeListener()

                // composite.registerListener(gTouchListener)
                // composite.registerListener(viewTouchListener)
                //   trackPadView.setOnTouchListener(composite)

                m=Sender(rMouseSender,hid,device)
                println("hihihi")
            }

        }

        BluetoothController.getDisconnector{
            val mainHandler = Handler(this.mainLooper)

            mainHandler.post(object : Runnable {
                override fun run() {
                    println("byebyebye")
                }
            })
        }

    }
    public override fun onStop() {
        super.onStop()
        BluetoothController.btHid?.unregisterApp()

        BluetoothController.hostDevice=null
        BluetoothController.btHid=null
    }


    private var mPtrCount = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_MOVE->{
                //implement moving of ptr
            }
            MotionEvent.ACTION_POINTER_DOWN->{
                mPtrCount++
            }
            MotionEvent.ACTION_POINTER_UP->{
                mPtrCount--
            }
        }
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }


    }

    override fun onShowPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent?): Boolean {return true}

    override fun onDown(e: MotionEvent?): Boolean { return true}

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {return true}

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if(mPtrCount==2) {
            var dy =0
            var dx:Int
            when {
                distanceY>0 -> { dy= -1}
                distanceY<0 -> { dy = 1}
                distanceY==0f -> { dy=0}
            }

            dx = when {
                distanceX>2 -> {
                    1
                }
                distanceX<-2 -> {
                    -1
                }
                else -> {
                    0
                }
            }
            if (dx > 127){ dx = 127}
            else if (dx < -127){ dx = -127}

            mouse?.sendScroll(dy, dx)
        }
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        mouse?.sendRightClick()
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        mouse?.sendAutoClick()
        mouse?.sendAutoClick()

        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        if (e?.action==MotionEvent.ACTION_UP){
            mouse?.sendLeftClickOff()
        }else if (e?.action==MotionEvent.ACTION_MOVE){
            mouse?.sendLeftClickOn()
        }
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        mouse?.sendAutoClick()
        return true
    }

}
