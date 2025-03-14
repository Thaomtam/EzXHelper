package com.github.kyuubiran.ezxhelper.utils

import android.widget.Toast
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit.setToastTag
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import de.robv.android.xposed.XposedBridge

open class Logger {
    /**
     * Cấp độ log, các log có cấp độ thấp hơn sẽ không được in ra
     * Có thể sử dụng kết hợp với BuildConfig.DEBUG / RELEASE
     */
    var logLevel: Int = VERBOSE

    /**
     * Tag của log
     */
    var logTag: String = "EZXHelper"


    /**
     * Có xuất log ra Xposed không
     */
    var logXp: Boolean = true
        internal set

    /**
     * Toast Tag
     */
    var toastTag: String? = null

    companion object LogLevel {
        const val VERBOSE = 0
        const val DEBUG = 1
        const val INFO = 2
        const val WARN = 3
        const val ERROR = 4
    }

    /**
     * In log với cấp độ: Info
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    open fun i(msg: String, thr: Throwable? = null) {
        if (logLevel > INFO) return
        android.util.Log.i(logTag, msg, thr)
    }

    /**
     * In log với cấp độ: Debug
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    open fun d(msg: String, thr: Throwable? = null) {
        if (logLevel > DEBUG) return
        android.util.Log.d(logTag, msg, thr)
    }


    /**
     * In log với cấp độ: Warn
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    open fun w(msg: String, thr: Throwable? = null) {
        if (logLevel > WARN) return
        android.util.Log.w(logTag, msg, thr)
    }


    /**
     * In log với cấp độ: Error
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    open fun e(msg: String, thr: Throwable? = null) {
        if (logLevel > ERROR) return
        android.util.Log.e(logTag, msg, thr)
    }


    /**
     * In log ra Xposed
     * @param level Cấp độ
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    open fun px(levelInt: Int, level: String, msg: String, thr: Throwable?) {
        if (logLevel > levelInt) return
        if (logXp) XposedBridge.log("[$level/$logTag] $msg: ${thr?.stackTraceToString()}")
    }


    /**
     * In log với cấp độ: Info
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun i(thr: Throwable, msg: String = "") {
        i(msg, thr)
    }

    /**
     * In log với cấp độ: Debug
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun d(thr: Throwable, msg: String = "") {
        d(msg, thr)
    }

    /**
     * In log với cấp độ: Warn
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun w(thr: Throwable, msg: String = "") {
        w(msg, thr)
    }

    /**
     * In log với cấp độ: Error
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun e(thr: Throwable, msg: String = "") {
        e(msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Info
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    fun ix(msg: String, thr: Throwable? = null) {
        i(msg, thr)
        px(INFO, "I", msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Info
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun ix(thr: Throwable, msg: String = "") {
        ix(msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Warn
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    fun wx(msg: String, thr: Throwable? = null) {
        w(msg, thr)
        px(WARN, "W", msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Warn
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun wx(thr: Throwable, msg: String = "") {
        wx(msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Debug
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    fun dx(msg: String, thr: Throwable? = null) {
        d(msg, thr)
        px(DEBUG, "D", msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Debug
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun dx(thr: Throwable, msg: String = "") {
        dx(msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Error
     * @param msg Thông báo
     * @param thr Ngoại lệ
     */
    fun ex(msg: String, thr: Throwable? = null) {
        e(msg, thr)
        px(ERROR, "E", msg, thr)
    }


    /**
     * In log ra Xposed với cấp độ: Error
     * @param thr Ngoại lệ
     * @param msg Thông báo
     */
    fun ex(thr: Throwable, msg: String = "") {
        ex(msg, thr)
    }
}

object Log {
    private val defaultLogger = Logger()
    private var logger: Logger? = null

    var currentLogger: Logger
        get() = logger ?: defaultLogger
        set(value) {
            logger = value
        }

    /**
     * Khi hiển thị Toast, nếu Toast trước đó chưa biến mất, có hủy Toast trước đó và hiển thị Toast mới không
     */
    var cancelLastToast: Boolean = false

    private var toast: Toast? = null

    fun i(msg: String, thr: Throwable? = null) {
        currentLogger.i(msg, thr)
    }

    fun d(msg: String, thr: Throwable? = null) {
        currentLogger.d(msg, thr)
    }

    fun w(msg: String, thr: Throwable? = null) {
        currentLogger.w(msg, thr)
    }

    fun e(msg: String, thr: Throwable? = null) {
        currentLogger.e(msg, thr)
    }

    fun ix(msg: String, thr: Throwable? = null) {
        currentLogger.ix(msg, thr)
    }

    fun wx(msg: String, thr: Throwable? = null) {
        currentLogger.wx(msg, thr)
    }

    fun dx(msg: String, thr: Throwable? = null) {
        currentLogger.dx(msg, thr)
    }

    fun ex(msg: String, thr: Throwable? = null) {
        currentLogger.ex(msg, thr)
    }

    fun i(thr: Throwable, msg: String = "") {
        currentLogger.i(thr, msg)
    }

    fun d(thr: Throwable, msg: String = "") {
        currentLogger.d(thr, msg)
    }

    fun w(thr: Throwable, msg: String = "") {
        currentLogger.w(thr, msg)
    }

    fun e(thr: Throwable, msg: String = "") {
        currentLogger.e(thr, msg)
    }

    fun ix(thr: Throwable, msg: String = "") {
        currentLogger.ix(thr, msg)
    }

    fun wx(thr: Throwable, msg: String = "") {
        currentLogger.wx(thr, msg)
    }

    fun dx(thr: Throwable, msg: String = "") {
        currentLogger.dx(thr, msg)
    }

    fun ex(thr: Throwable, msg: String = "") {
        currentLogger.ex(thr, msg)
    }

    /**
     * Hiển thị một Toast
     *
     * Cần khởi tạo appContext trước khi sử dụng
     *
     * Nếu không thiết lập TOAST_TAG
     * thì sẽ không hiển thị tiền tố
     * @see setToastTag
     */
    fun toast(msg: String, duration: Int = Toast.LENGTH_SHORT) = runOnMainThread {
        if (cancelLastToast) toast?.cancel()
        toast = null
        toast = Toast.makeText(appContext, null, duration).apply {
            setText(if (currentLogger.toastTag != null) "${currentLogger.toastTag}: $msg" else msg)
            show()
        }
    }

    fun toast(msg: String, vararg formats: String, duration: Int = Toast.LENGTH_SHORT) =
        toast(msg.format(*formats), duration)

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.i để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see i
     */
    inline fun <R> Result<R>.logiIfThrow(msg: String = "", then: ((Throwable) -> Unit) = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.i(it, msg)
            then(it)
        }

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.ix để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see ix
     */
    inline fun <R> Result<R>.logixIfThrow(msg: String = "", then: ((Throwable) -> Unit) = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.i(it, msg)
            then(it)
        }

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.d để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see d
     */
    inline fun <R> Result<R>.logdIfThrow(msg: String = "", then: (Throwable) -> Unit = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.d(it, msg)
            then(it)
        }

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.dx để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see dx
     */
    inline fun <R> Result<R>.logdxIfThrow(msg: String = "", then: (Throwable) -> Unit = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.dx(it, msg)
            then(it)
        }

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.w để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see w
     */
    inline fun <R> Result<R>.logwIfThrow(msg: String = "", then: (Throwable) -> Unit = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.w(it, msg)
            then(it)
        }

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.wx để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see wx
     */
    inline fun <R> Result<R>.logwxIfThrow(msg: String = "", then: (Throwable) -> Unit = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.wx(it, msg)
            then(it)
        }

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.e để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see e
     */
    inline fun <R> Result<R>.logeIfThrow(msg: String = "", then: (Throwable) -> Unit = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.e(it, msg)
            then(it)
        }

    /**
     * Hàm mở rộng để sử dụng với runCatching
     * Nếu có ngoại lệ xảy ra, gọi Log.ex để ghi lại
     * @param msg Thông báo
     * @param then Hàm được thực thi khi có ngoại lệ
     * @see runCatching
     * @see ex
     */
    inline fun <R> Result<R>.logexIfThrow(msg: String = "", then: (Throwable) -> Unit = {}) =
        this.exceptionOrNull()?.let {
            currentLogger.ex(it, msg)
            then(it)
        }
}