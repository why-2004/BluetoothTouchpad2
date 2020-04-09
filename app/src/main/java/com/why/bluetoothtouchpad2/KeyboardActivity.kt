package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.renderscript.ScriptGroup
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.why.bluetoothtouchpad2.bluetooth.BluetoothController
import com.why.bluetoothtouchpad2.bluetooth.KeyboardSender
import com.why.bluetoothtouchpad2.bluetooth.Sender

class KeyboardActivity : AppCompatActivity() {
    var m: Sender? = null
    private var rKeyboardSender : KeyboardSender? = null
    lateinit var imm:InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)

    }
    public override fun onStop() {
        super.onStop()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
        if (!Main.isAppRunning(applicationContext, "com.why.bluetoothtouchpad2")) {
            BluetoothController.btHid?.unregisterApp()

            BluetoothController.hostDevice = null
            BluetoothController.btHid = null
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        println(keyCode)
        println(event?.keyCode)
        return if(rKeyboardSender !=null && event !=null) {
            false
            //super.onKeyDown(keyCode, event)

        } else super.onKeyDown(keyCode, event)


    }

    private var modifierCheckedState : Int =0
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        return if(rKeyboardSender !=null && event !=null) {
            val rvalue: Boolean? = rKeyboardSender?.sendKeyboard(keyCode, event,modifierCheckedState)

            if (rvalue == true){ true}
            else{ super.onKeyUp(keyCode, event)}

        } else super.onKeyUp(keyCode, event)


    }

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
        findViewById<Button>(R.id.up).setOnTouchListener{ v, m->
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_UP,
                        KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_UP),modifierCheckedState
                    )
                }
                MotionEvent.ACTION_UP->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_UP,
                        KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_UP),modifierCheckedState
                    )
                }
            }
            return@setOnTouchListener true
        }
        findViewById<Button>(R.id.left).setOnTouchListener{ v, m->
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_LEFT,
                        KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_LEFT),modifierCheckedState
                    )
                }
                MotionEvent.ACTION_UP->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_LEFT,
                        KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_LEFT),modifierCheckedState
                    )
                }
            }
            return@setOnTouchListener true
        }
        findViewById<Button>(R.id.right).setOnTouchListener{ v, m->
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_RIGHT,
                        KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_RIGHT),modifierCheckedState
                    )
                }
                MotionEvent.ACTION_UP->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_RIGHT,
                        KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_RIGHT),modifierCheckedState
                    )
                }
            }
            return@setOnTouchListener true
        }

        findViewById<Button>(R.id.down).setOnTouchListener{ v, m->
            when(m.action){
                MotionEvent.ACTION_DOWN->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_DOWN,
                        KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN),modifierCheckedState
                    )
                }
                MotionEvent.ACTION_UP->{
                    rKeyboardSender?.sendKeyboard(KeyEvent.KEYCODE_DPAD_DOWN,
                        KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DPAD_DOWN),modifierCheckedState
                    )
                }
            }
            return@setOnTouchListener true
        }

        BluetoothController.getDisconnector {
            val mainHandler = Handler(this.mainLooper)

            mainHandler.post { println("zaijian") }
        }
    }

    override fun onRestart() {
        super.onRestart()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }

    fun moveLeft(view: View) {
        startActivity(Intent(this,MouseActivity::class.java))

    }
    fun moveRight(view: View) {
        startActivity(Intent(this,PointerActivity::class.java))

    }






}
