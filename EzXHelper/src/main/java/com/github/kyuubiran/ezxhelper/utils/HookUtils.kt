package com.github.kyuubiran.ezxhelper.utils

import com.github.kyuubiran.ezxhelper.init.InitFields
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.Unhook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XCallback
import java.lang.reflect.Constructor
import java.lang.reflect.Method

typealias Hooker = (param: XC_MethodHook.MethodHookParam) -> Unit
typealias ReplaceHooker = (param: XC_MethodHook.MethodHookParam) -> Any?

/**
 * Hàm mở rộng để hook phương thức/constructor
 * @param hookCallback [XC_MethodHook]
 * @return unhook [XC_MethodHook.Unhook]
 */
fun Method.hookMethod(hookCallback: XC_MethodHook): XC_MethodHook.Unhook {
    return XposedBridge.hookMethod(this, hookCallback)
}

fun Constructor<*>.hookMethod(hookCallback: XC_MethodHook): XC_MethodHook.Unhook {
    return XposedBridge.hookMethod(this, hookCallback)
}

inline fun hooker(crossinline hookCallback: Hooker): Hooker = object : Hooker {
    override fun invoke(param: XC_MethodHook.MethodHookParam) {
        hookCallback(param)
    }
}

inline fun replaceHooker(crossinline hookCallback: ReplaceHooker):
        ReplaceHooker = object : ReplaceHooker {
    override fun invoke(param: XC_MethodHook.MethodHookParam): Any? {
        return hookCallback(param)
    }
}


/**
 * Hàm mở rộng để hook trước khi phương thức thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hook [Hooker] Triển khai cụ thể của hook
 * @return unhook [XC_MethodHook.Unhook]
 */
fun Method.hookBefore(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hook: Hooker
): XC_MethodHook.Unhook {
    return this.hookMethod(object : XC_MethodHook(priority) {
        override fun beforeHookedMethod(param: MethodHookParam) = try {
            hook(param)
        } catch (thr: Throwable) {
            Log.ex(thr)
        }
    })
}

fun Method.hookBefore(hooker: Hooker) = this.hookBefore(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook trước khi nhiều phương thức thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hooker [Hooker] Triển khai cụ thể của hook
 * @return unhooks Array<[XC_MethodHook.Unhook]>
 */
fun Array<Method>.hookBefore(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): Array<XC_MethodHook.Unhook> {
    return this.map { it.hookBefore(priority, hooker) }.toTypedArray()
}

fun Array<Method>.hookBefore(hooker: Hooker) = this.hookBefore(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

fun Iterable<Method>.hookBefore(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): List<XC_MethodHook.Unhook> {
    return this.map { it.hookBefore(priority, hooker) }
}

fun Iterable<Method>.hookBefore(hooker: Hooker) = this.hookBefore(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook trước khi constructor thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hook [Hooker] Triển khai cụ thể của hook
 * @return unhook [XC_MethodHook.Unhook]
 */
fun Constructor<*>.hookBefore(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hook: Hooker
): XC_MethodHook.Unhook {
    return this.hookMethod(object : XC_MethodHook(priority) {
        override fun beforeHookedMethod(param: MethodHookParam) = try {
            hook(param)
        } catch (thr: Throwable) {
            Log.ex(thr)
        }
    })
}

fun Constructor<*>.hookBefore(hooker: Hooker) = this.hookBefore(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook trước khi nhiều constructor thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hooker [Hooker] Triển khai cụ thể của hook
 * @return unhooks Array<[XC_MethodHook.Unhook]>
 */
fun Array<Constructor<*>>.hookBefore(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): Array<XC_MethodHook.Unhook> {
    return this.map { it.hookBefore(priority, hooker) }.toTypedArray()
}

fun Array<Constructor<*>>.hookBefore(hooker: Hooker) = this.hookBefore(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

@JvmName("hookConstructorBefore")
fun Iterable<Constructor<*>>.hookBefore(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): List<XC_MethodHook.Unhook> {
    return this.map { it.hookBefore(priority, hooker) }
}

@JvmName("hookConstructorBefore")
fun Iterable<Constructor<*>>.hookBefore(hooker: Hooker) = this.hookBefore(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook sau khi phương thức thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hooker [Hooker] Triển khai cụ thể của hook
 * @return unhook [XC_MethodHook.Unhook]
 */
fun Method.hookAfter(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): XC_MethodHook.Unhook {
    return this.hookMethod(object : XC_MethodHook(priority) {
        override fun afterHookedMethod(param: MethodHookParam) = try {
            hooker(param)
        } catch (thr: Throwable) {
            Log.ex(thr)
        }
    })
}

fun Method.hookAfter(hooker: Hooker) = this.hookAfter(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook sau khi nhiều phương thức thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hooker [Hooker] Triển khai cụ thể của hook
 * @return unhooks Array<[XC_MethodHook.Unhook]>
 */
fun Array<Method>.hookAfter(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): Array<XC_MethodHook.Unhook> {
    return this.map { it.hookAfter(priority, hooker) }.toTypedArray()
}

fun Array<Method>.hookAfter(hooker: Hooker) = this.hookAfter(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

fun Iterable<Method>.hookAfter(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): List<XC_MethodHook.Unhook> {
    return this.map { it.hookAfter(priority, hooker) }
}

fun Iterable<Method>.hookAfter(hooker: Hooker) = this.hookAfter(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook sau khi constructor thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hooker [Hooker] Triển khai cụ thể của hook
 * @return unhook [XC_MethodHook.Unhook]
 */
fun Constructor<*>.hookAfter(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): XC_MethodHook.Unhook {
    return this.hookMethod(object : XC_MethodHook(priority) {
        override fun afterHookedMethod(param: MethodHookParam) = try {
            hooker(param)
        } catch (thr: Throwable) {
            Log.ex(thr)
        }
    })
}

fun Constructor<*>.hookAfter(hooker: Hooker) = this.hookAfter(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook sau khi nhiều constructor thực thi
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hooker [Hooker] Triển khai cụ thể của hook
 * @return unhooks Array<[XC_MethodHook.Unhook]>
 */
fun Array<Constructor<*>>.hookAfter(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): Array<XC_MethodHook.Unhook> {
    return this.map { it.hookAfter(priority, hooker) }.toTypedArray()
}

fun Array<Constructor<*>>.hookAfter(hooker: Hooker) = this.hookAfter(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

@JvmName("hookConstructorAfter")
fun Iterable<Constructor<*>>.hookAfter(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: Hooker
): List<XC_MethodHook.Unhook> {
    return this.map { it.hookAfter(priority, hooker) }
}

@JvmName("hookConstructorAfter")
fun Iterable<Constructor<*>>.hookAfter(hooker: Hooker) = this.hookAfter(
    XCallback.PRIORITY_DEFAULT,
    hooker
)

/**
 * Hàm mở rộng để hook phương thức 
 * @param priority Độ ưu tiên, mặc định là 50
 * @param obj Giá trị trả về
 * @return unhook [XC_MethodHook.Unhook]
 */
fun Method.hookReturnConstant(priority: Int = XCallback.PRIORITY_DEFAULT, obj: Any?): Unhook =
    XposedBridge.hookMethod(this, XC_MethodReplacement.returnConstant(priority, obj))

fun Method.hookReturnConstant(obj: Any?) = this.hookReturnConstant(XCallback.PRIORITY_DEFAULT, obj)

/**
 * Hàm mở rộng để hook phương thức trong mảng 
 * @param priority Độ ưu tiên, mặc định là 50
 * @param obj Giá trị trả về
 * @return unhooks Array<[XC_MethodHook.Unhook]>
 */
fun Array<Method>.hookReturnConstant(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    obj: Any?
): Array<Unhook> =
    this.map { XposedBridge.hookMethod(it, XC_MethodReplacement.returnConstant(priority, obj)) }
        .toTypedArray()

fun Array<Method>.hookReturnConstant(obj: Any?) =
    this.hookReturnConstant(XCallback.PRIORITY_DEFAULT, obj)

fun List<Method>.hookReturnConstant(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    obj: Any?
): List<Unhook> =
    this.map { XposedBridge.hookMethod(it, XC_MethodReplacement.returnConstant(priority, obj)) }

fun List<Method>.hookReturnConstant(obj: Any?) =
    this.hookReturnConstant(XCallback.PRIORITY_DEFAULT, obj)

/**
 * Hàm mở rộng để hook constructor 
 * @param priority Độ ưu tiên, mặc định là 50
 * @param obj Giá trị trả về
 * @return unhook [XC_MethodHook.Unhook]
 */
fun Constructor<*>.hookReturnConstant(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    obj: Any?
): Unhook =
    XposedBridge.hookMethod(this, XC_MethodReplacement.returnConstant(priority, obj))

fun Constructor<*>.hookReturnConstant(obj: Any?) =
    this.hookReturnConstant(XCallback.PRIORITY_DEFAULT, obj)

fun Array<Constructor<*>>.hookReturnConstant(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    obj: Any?
): Array<Unhook> =
    this.map { XposedBridge.hookMethod(it, XC_MethodReplacement.returnConstant(priority, obj)) }
        .toTypedArray()

fun Array<Constructor<*>>.hookReturnConstant(obj: Any?) =
    this.hookReturnConstant(XCallback.PRIORITY_DEFAULT, obj)

@JvmName("hookConstructorReturnConstant")
fun List<Constructor<*>>.hookReturnConstant(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    obj: Any?
): List<Unhook> =
    this.map { XposedBridge.hookMethod(it, XC_MethodReplacement.returnConstant(priority, obj)) }

@JvmName("hookConstructorReturnConstant")
fun List<Constructor<*>>.hookReturnConstant(obj: Any?) =
    this.hookReturnConstant(XCallback.PRIORITY_DEFAULT, obj)

/**
 * Lớp factory cho Hook
 */
class XposedHookFactory(priority: Int = XCallback.PRIORITY_DEFAULT) : XC_MethodHook(priority) {
    private var beforeMethod: Hooker? = null
    private var afterMethod: Hooker? = null

    override fun beforeHookedMethod(param: MethodHookParam) {
        beforeMethod?.invoke(param)
    }

    override fun afterHookedMethod(param: MethodHookParam) {
        afterMethod?.invoke(param)
    }

    /**
     * Hook trước khi phương thức thực thi
     */
    fun before(before: Hooker) {
        this.beforeMethod = before
    }

    /**
     * Hook sau khi phương thức thực thi
     */
    fun after(after: Hooker) {
        this.afterMethod = after
    }
}

/**
 * Hàm mở rộng để hook phương thức
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hook Hàm hook
 *
 * Sử dụng dưới dạng:
 * before { }
 *
 * after { }
 *
 * Cả hai đều là tùy chọn
 * @return Unhook
 * @see XposedHookFactory
 */
fun Method.hookMethod(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hook: XposedHookFactory.() -> Unit
): XC_MethodHook.Unhook {
    val factory = XposedHookFactory(priority)
    hook(factory)
    return this.hookMethod(factory)
}

/**
 * Hàm mở rộng để hook constructor
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hook Hàm hook
 *
 * Sử dụng dưới dạng:
 * before { }
 *
 * after { }
 *
 * Cả hai đều là tùy chọn
 * @return Unhook
 * @see XposedHookFactory
 */
fun Constructor<*>.hookMethod(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hook: XposedHookFactory.() -> Unit
): XC_MethodHook.Unhook {
    val factory = XposedHookFactory(priority)
    hook(factory)
    return this.hookMethod(factory)
}

/**
 * Hàm mở rộng để hook nhiều phương thức
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hook Hàm hook
 *
 * Sử dụng dưới dạng:
 * before { }
 *
 * after { }
 *
 * Cả hai đều là tùy chọn
 * @return Array<Unhook>
 * @see XposedHookFactory
 */
fun Array<Method>.hookMethod(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hook: XposedHookFactory.() -> Unit
): Array<XC_MethodHook.Unhook> {
    return this.map { it.hookMethod(priority, hook) }.toTypedArray()
}

fun Iterable<Method>.hookMethod(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hook: XposedHookFactory.() -> Unit
): List<XC_MethodHook.Unhook> {
    return this.map { it.hookMethod(priority, hook) }
}

/**
 * Hàm mở rộng để hook nhiều constructor
 * @param priority Độ ưu tiên, mặc định là 50
 * @param hooker Hàm hook
 *
 * Sử dụng dưới dạng:
 * before { }
 *
 * after { }
 *
 * Cả hai đều là tùy chọn
 * @return Array<Unhook>
 * @see XposedHookFactory
 */
fun Array<Constructor<*>>.hookMethod(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: XposedHookFactory.() -> Unit
): Array<XC_MethodHook.Unhook> {
    return this.map { it.hookMethod(priority, hooker) }.toTypedArray()
}

@JvmName("hookConstructor")
fun Iterable<Constructor<*>>.hookMethod(
    priority: Int = XCallback.PRIORITY_DEFAULT,
    hooker: XposedHookFactory.() -> Unit
): List<XC_MethodHook.Unhook> {
    return this.map { it.hookMethod(priority, hooker) }
}

/**
 * Thực thi tất cả các unhook
 */
fun Array<XC_MethodHook.Unhook>.unhookAll() {
    this.forEach { it.unhook() }
}

fun Iterable<XC_MethodHook.Unhook>.unhookAll() {
    this.forEach { it.unhook() }
}
