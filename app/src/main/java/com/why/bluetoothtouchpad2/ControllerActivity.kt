package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.why.bluetoothtouchpad2.Main.mouse
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.KeyboardReport
import com.why.bluetoothtouchpad2.bluetooth.KeyboardSender
import com.why.bluetoothtouchpad2.bluetooth.Sender
import kotlinx.android.synthetic.main.activity_controller.*


class ControllerActivity : Activity() {
    var m: Sender? = null
    private var rKeyboardSender : KeyboardSender? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)




    }
    public override fun onStop() {
        super.onStop()
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
        var view: View? = this.currentFocus
        if (view == null) {
            view = View(this)
        }

        if (!Main.isAppRunning(applicationContext, "com.why.bluetoothtouchpad2")) {
            BluetoothController.btHid?.unregisterApp()

            BluetoothController.hostDevice = null
            BluetoothController.btHid = null
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
    }


    private var modifierCheckedState : Int =0

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        BluetoothController.getSender { hid, device ->
            println("keybord")
            val mainHandler = Handler(this.mainLooper)
            mainHandler.post {

                rKeyboardSender= KeyboardSender(hid,device)

                m = Sender(Main.mouse!!, hid, device)
                println("keyboard start!")
            }

        }
        findViewById<Button>(R.id.jitter).setOnTouchListener{ v, m->
            Main.mp?.start()
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    mouse?.sendAutoClick()
                }
                MotionEvent.ACTION_UP->{

                }
            }
            return@setOnTouchListener true
        }



        BluetoothController.getDisconnector {
            val mainHandler = Handler(this.mainLooper)

            mainHandler.post { println("zaijian") }
        }
    }

    fun rush(view: View)         {
        Main.mp?.start()
        Thread {
            for (i in edittext.text) {
                println(i)
                when {
                    i.isUpperCase() -> {
                        rKeyboardSender?.keyboardReport?.leftShift = true
                        rKeyboardSender?.sendKeyOn(i.toInt() - 61)
                    }
                    i.isLowerCase() -> {
                        rKeyboardSender?.keyboardReport?.leftShift = false
                        rKeyboardSender?.sendKeyOn(i.toInt() - 93)
                    }
                    i.isDigit() -> {
                        rKeyboardSender?.keyboardReport?.leftShift = false
                        rKeyboardSender?.sendKeyOn(i.toInt() - 18)
                    }
                    else -> {
                        when (i) {
                            '!' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(30)
                            }

                            '@' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(31)
                            }
                            '#' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(32)
                            }
                            '$' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(33)
                            }
                            '%' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(34)
                            }
                            '^' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(35)
                            }
                            '&' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(36)

                            }
                            '*' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(37)
                            }
                            '(' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(38)
                            }
                            ')' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(39)
                            }
                            '+' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(46)
                            }
                            '=' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(46)
                            }
                            '-' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(45)
                            }
                            '_' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(45)
                            }
                            ' ' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(44)
                            }
                            '.' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(55)
                            }
                            '>' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(55)
                            }
                            ',' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(54)
                            }
                            '<' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(54)
                            }
                            '/' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(56)
                            }
                            '\\' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(49)
                            }
                            '|' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(49)
                            }
                            '`' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(53)
                            }
                            '~' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(53)

                            }
                            '?' -> {
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(56)
                            }
                            '…'->{
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(55)
                                rKeyboardSender?.sendKeyOn(55)
                                rKeyboardSender?.sendKeyOn(55)
                            }
                            ';'->{
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(51)
                            }
                            ':'->{
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(51)
                            }
                            '\''->{
                                rKeyboardSender?.keyboardReport?.leftShift = false
                                rKeyboardSender?.sendKeyOn(52)
                            }
                            '"'->{
                                rKeyboardSender?.keyboardReport?.leftShift = true
                                rKeyboardSender?.sendKeyOn(52)
                            }
                        }
                    }
                }
                Thread.sleep(10)
                rKeyboardSender?.sendKeyOff()
                Thread.sleep(10)
            }
            rKeyboardSender?.keyboardReport?.leftShift=false
            rKeyboardSender?.sendKeyOff()
        }.start()


    }
}
