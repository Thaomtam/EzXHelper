package com.github.kyuubiran.ezxhelper.utils

import android.app.Activity
import dalvik.system.BaseDexClassLoader
import java.util.*
import kotlin.system.exitProcess

/**
 * Thử thực thi một khối mã, nếu thành công trả về true, thất bại trả về false
 * @param block Khối mã cần thực thi
 * @return true nếu thành công, false nếu thất bại
 */
inline fun tryOrFalse(block: () -> Unit): Boolean = try {
    block()
    true
} catch (thr: Throwable) {
    false
}

/**
 * Thử thực thi một khối mã, nếu thất bại thì ghi log
 * @param block Khối mã cần thực thi
 */
inline fun tryOrLog(block: () -> Unit) = try {
    block()
} catch (thr: Throwable) {
    Log.e(thr)
}

/**
 * Thử thực thi một khối mã, nếu thành công trả về true, thất bại trả về false và ghi log
 * @param block Khối mã cần thực thi
 * @return true nếu thành công, false nếu thất bại
 */
inline fun tryOrLogFalse(block: () -> Unit): Boolean = try {
    block()
    true
} catch (thr: Throwable) {
    Log.e(thr)
    false
}

/**
 * Thử thực thi một khối mã, nếu thành công trả về kết quả của khối mã, thất bại trả về null
 * @param block Khối mã cần thực thi
 * @return Giá trị trả về của khối mã nếu thành công, null nếu thất bại
 */
inline fun <T> tryOrNull(block: () -> T?): T? = try {
    block()
} catch (thr: Throwable) {
    null
}

/**
 * Thử thực thi một khối mã, nếu thành công trả về kết quả của khối mã, thất bại trả về null và ghi log
 * @param block Khối mã cần thực thi
 * @return Giá trị trả về của khối mã nếu thành công, null nếu thất bại
 */
inline fun <T> tryOrLogNull(block: () -> T?): T? = try {
    block()
} catch (thr: Throwable) {
    Log.e(thr)
    null
}

/**
 * Hàm mở rộng để giữ lại các phần tử trong danh sách có thể thay đổi thỏa mãn điều kiện
 * @param predicate Điều kiện
 */
inline fun <E> MutableList<E>.retainIf(predicate: ((E) -> Boolean)) {
    this.filter { elem -> predicate(elem) }.forEach { this.remove(it) }
}

/**
 * Hàm mở rộng để giữ lại các phần tử trong danh sách có thể thay đổi thỏa mãn điều kiện và trả về danh sách đó
 * @param predicate Điều kiện
 * @return Danh sách có thể thay đổi sau khi giữ lại các phần tử thỏa mãn điều kiện
 */
inline fun <E> MutableList<E>.applyRetainIf(predicate: (E) -> Boolean): MutableList<E> {
    this.retainIf(predicate)
    return this
}

/**
 * Hàm mở rộng để giữ lại các phần tử trong tập hợp có thể thay đổi thỏa mãn điều kiện
 * @param predicate Điều kiện
 */
inline fun <E> MutableSet<E>.retainIf(predicate: (E) -> Boolean) {
    this.filter { elem -> predicate(elem) }.forEach { this.remove(it) }
}

/**
 * Hàm mở rộng để giữ lại các phần tử trong tập hợp có thể thay đổi thỏa mãn điều kiện và trả về tập hợp đó
 * @param predicate Điều kiện
 * @return Tập hợp có thể thay đổi sau khi giữ lại các phần tử thỏa mãn điều kiện
 */
inline fun <E> MutableSet<E>.applyRetainIf(predicate: (E) -> Boolean): MutableSet<E> {
    this.retainIf(predicate)
    return this
}

/**
 * Hàm mở rộng để giữ lại các phần tử trong từ điển có thể thay đổi thỏa mãn điều kiện
 * @param predicate Điều kiện
 */
inline fun <K, V> MutableMap<K, V>.retainIf(predicate: (K, V) -> Boolean) {
    this.filter { (key, value) -> predicate(key, value) }.forEach { this.remove(it.key) }
}

/**
 * Hàm mở rộng để giữ lại các phần tử trong từ điển có thể thay đổi thỏa mãn điều kiện và trả về từ điển đó
 * @param predicate Điều kiện
 * @return Từ điển có thể thay đổi sau khi giữ lại các phần tử thỏa mãn điều kiện
 */
inline fun <K, V> MutableMap<K, V>.applyRetainIf(predicate: (K, V) -> Boolean): MutableMap<K, V> {
    this.retainIf(predicate)
    return this
}

/**
 * Hàm mở rộng để xóa các phần tử trong từ điển có thể thay đổi thỏa mãn điều kiện
 * @param predicate Điều kiện
 */
inline fun <K, V> MutableMap<K, V>.removeIf(predicate: (K, V) -> Boolean) {
    this.filter { (key, value) -> predicate(key, value) }.forEach { this.remove(it.key) }
}

/**
 * Hàm mở rộng để xóa các phần tử trong từ điển có thể thay đổi thỏa mãn điều kiện và trả về từ điển đó
 * @param predicate Điều kiện
 * @return Từ điển có thể thay đổi sau khi xóa các phần tử thỏa mãn điều kiện
 */
inline fun <K, V> MutableMap<K, V>.applyRemoveIf(
    predicate: (K, V) -> Boolean
): MutableMap<K, V> {
    this.removeIf(predicate)
    return this
}

/**
 * Lấy từ BiliRoaming
 * Tìm DexClassLoader
 * @see `https://github.com/yujincheng08/BiliRoaming`
 */
inline fun ClassLoader.findDexClassLoader(crossinline delegator: (BaseDexClassLoader) -> BaseDexClassLoader = { x -> x }): BaseDexClassLoader? {
    var classLoader = this
    while (classLoader !is BaseDexClassLoader) {
        if (classLoader.parent != null) classLoader = classLoader.parent
        else return null
    }
    return delegator(classLoader)
}

/**
 * Lấy từ BiliRoaming
 * Lấy tất cả tên lớp
 * @see `https://github.com/yujincheng08/BiliRoaming`
 */
fun ClassLoader.getAllClassesList(delegator: (BaseDexClassLoader) -> BaseDexClassLoader = { loader -> loader }): List<String> =
    findDexClassLoader(delegator)?.getObjectOrNull("pathList")
        ?.getObjectOrNullAs<Array<Any>>("dexElements")
        ?.flatMap {
            it.getObjectOrNull("dexFile")?.invokeMethodAutoAs<Enumeration<String>>("entries")?.toList()
                .orEmpty()
        }.orEmpty()

/**
 * Khởi động lại ứng dụng chủ
 */
fun restartHostApp(activity: Activity) {
    val pm = activity.packageManager
    val intent = pm.getLaunchIntentForPackage(activity.packageName)
    activity.finishAffinity()
    activity.startActivity(intent)
    exitProcess(0)
}

/**
 * Hàm mở rộng để kiểm tra xem các lớp có giống nhau không (dùng để kiểm tra tham số)
 *
 * Ví dụ: fun foo(a: Boolean, b: Int) { }
 * foo.parameterTypes.sameAs(*array)
 * foo.parameterTypes.sameAs(Boolean::class.java, Int::class.java)
 * foo.parameterTypes.sameAs("boolean", "int")
 * foo.parameterTypes.sameAs(Boolean::class.java, "int")
 *
 * @param other Các lớp khác (hỗ trợ String hoặc Class<*>)
 * @return có giống nhau không
 */
fun Array<Class<*>>.sameAs(vararg other: Any): Boolean {
    if (this.size != other.size) return false
    for (i in this.indices) {
        when (val otherClazz = other[i]) {
            is Class<*> -> {
                if (this[i] != otherClazz) return false
            }
            is String -> {
                if (this[i].name != otherClazz) return false
            }
            else -> {
                throw IllegalArgumentException("Only support Class<*> or String")
            }
        }
    }
    return true
}

fun List<Class<*>>.sameAs(vararg other: Any): Boolean {
    if (this.size != other.size) return false
    for (i in this.indices) {
        when (val otherClazz = other[i]) {
            is Class<*> -> {
                if (this[i] != otherClazz) return false
            }
            is String -> {
                if (this[i].name != otherClazz) return false
            }
            else -> {
                throw IllegalArgumentException("Only support Class<*> or String")
            }
        }
    }
    return true
}
