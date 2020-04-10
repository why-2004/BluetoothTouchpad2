package com.why.bluetoothtouchpad2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context


import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import java.io.File

class TutorialActivity : Activity() {
    lateinit var f:File

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

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
        f = this.filesDir.resolve("disclaimer.txt")
        if (( getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)==null){
            findViewById<TextView>(R.id.tutorial_text).text = getString(R.string.tutorial_string)+"\n"+getString(R.string.gyro_not_avail)
        }
        if (f.exists()&&f.readText()=="...") {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }



    }

    fun klik(view: View) {
        f.createNewFile()
        f.writeText("...")
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }
}
