package com.coral.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 基于 kotlin-android-extensions 插件自动生成
        btn_click.text = "我是使用 Kotlin 改变的按钮"
        btn_click.setOnClickListener {
            Toast.makeText(this, "Main Page setOnClickListener", Toast.LENGTH_LONG).show()
        }

        val people = People("Coral")
        tv_msg.text = people.name
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Main Page onDestroy")
    }

}
