package com.github.kyuubiran.ezxhelper.utils

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit

val mainHandler: Handler by lazy {
    Handler(Looper.getMainLooper())
}

val runtimeProcess: Runtime by lazy {
    Runtime.getRuntime()
}

/**
 * Hàm mở rộng để thực thi hàm trên luồng chính, như cập nhật UI, hiển thị Toast, v.v.
 */
fun Runnable.postOnMainThread() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        this.run()
    } else {
        mainHandler.post(this)
    }
}

fun runOnMainThread(runnable: Runnable) {
    runnable.postOnMainThread()
}

/**
 * Hàm mở rộng để hiển thị một Toast
 * @param msg Thông báo hiển thị trong Toast
 * @param length Thời gian hiển thị của Toast
 */
fun Context.showToast(msg: String, length: Int = Toast.LENGTH_SHORT) = runOnMainThread {
    Toast.makeText(this, msg, length).show()
}

/**
 * Hàm mở rộng để hiển thị một Toast
 * @param msg Thông báo hiển thị trong Toast
 * @param args Các tham số định dạng
 * @param length Thời gian hiển thị của Toast
 */
fun Context.showToast(msg: String, vararg args: Any?, length: Int = Toast.LENGTH_SHORT) = runOnMainThread {
    Toast.makeText(this, msg.format(args), length).show()
}

/**
 * Hàm mở rộng để thêm đường dẫn tài nguyên của module vào Context.resources, cho phép truy cập trực tiếp tài nguyên thông qua R.xx.xxx
 * @see EzXHelperInit.addModuleAssetPath
 */
fun Context.addModuleAssetPath() {
    EzXHelperInit.addModuleAssetPath(this)
}

/**
 * Hàm mở rộng để thêm đường dẫn tài nguyên của module vào resources, cho phép truy cập trực tiếp tài nguyên thông qua R.xx.xxx
 * @see EzXHelperInit.addModuleAssetPath
 */
fun Resources.addModuleAssetPath() {
    EzXHelperInit.addModuleAssetPath(this)
}