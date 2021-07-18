package com.coral.kotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.sample.sunflower.GardenActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    // 2.1 是否展示省电优化设置入口
    fun batteryOptimizationsVisible() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    // 2.2 检测是否在省电优化白名单 API 23 6.0（状态监测）
    fun isBatteryOptimizationsIgnored(context: Context): Boolean {
        if (batteryOptimizationsVisible()) {
            var isIgnoring = false
            val powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (powerManager != null) {
                isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
                Log.e("bbb", "ignore = $isIgnoring")
            }
            return isIgnoring
        }
        return true
    }

    val REQUEST_CODE_BATTERY_OPTIMIZE = 0x11

    /**
     * 2. 申请加入电池优化白名单（注：要添加权限 android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS />）
     */
    fun requestIgnoreBatteryOptimizations(context: Context) {
        if (batteryOptimizationsVisible()) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + context.packageName)
                (context as Activity).startActivityForResult(intent, REQUEST_CODE_BATTERY_OPTIMIZE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 更新电池优化的开启状态
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZE) {
            btn_battery_optim.postDelayed({
                val result = isBatteryOptimizationsIgnored(this)
                val s = if (result) "已允许" else "未允许"
                Log.e("Battery", s);

                Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

            }, 1000)

//            val result = isBatteryOptimizationsIgnored(this)
//            val s = if (result) "已允许" else "未允许"
//            Log.e("Battery", s);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 基于 kotlin-android-extensions 插件自动生成
        btn_click.text = "我是使用 Kotlin 改变的按钮"
        btn_click.setOnClickListener {
            toast(this, "Main Page setOnClickListener")
        }

        btn_battery_optim.setOnClickListener {
            if (isBatteryOptimizationsIgnored(this)) {
                Log.e("Battery", "已允许");
            } else {
                requestIgnoreBatteryOptimizations(this)
            }
        }

        val people = People("Coral")
        tv_msg.text = people.name

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PageAdapter()
        recyclerView.adapter = adapter
        adapter.setData(listOf<PageData>(
            PageData("Main", GardenActivity::class.java),
            PageData("Garden", GardenActivity::class.java)))
    }

    // 扩展函数
    fun MainActivity.toastExt(context: Context, msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, msg, duration).show()
    }

    companion object {
        // 静态方法
        fun toast(context: Context, msg: String, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(context, msg, duration).show()
        }
    }

    class PageAdapter: RecyclerView.Adapter<ViewHolder>() {

        private var list = mutableListOf<PageData>()

        fun setData(list: List<PageData>) {
            this.list.addAll(list)
            notifyDataSetChanged()
        }

        fun addData(data: PageData) {
            this.list.add(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val tv = TextView(parent.context)
            tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90)
            tv.gravity = Gravity.CENTER
            tv.setTextColor(parent.context.resources.getColor(R.color.white))
            return ViewHolder(tv)
        }

        override fun getItemCount(): Int = this.list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindData(position, list[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(pos: Int, item: PageData) {
            // 强制装换
            var tv = itemView as TextView
            tv.text = item.name

            val bgColor = if (pos % 2 == 0) "#00ff00" else "#0000ff"  // Kotlin 没有三目表达式，使用此种方式表示
            tv.setBackgroundColor(Color.parseColor(bgColor))

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, item.clazz)
                itemView.context.startActivity(intent)
            }
        }
    }

    data class PageData(val name: String, val clazz: Class<*>?)

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Main Page onDestroy")
    }

}
