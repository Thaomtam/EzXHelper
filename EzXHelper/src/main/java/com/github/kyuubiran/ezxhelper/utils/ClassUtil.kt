package com.github.kyuubiran.ezxhelper.utils

import com.github.kyuubiran.ezxhelper.init.InitFields
import de.robv.android.xposed.XposedHelpers

/**
 * Tải lớp thông qua module
 * @param clzName Tên lớp
 * @param clzLoader ClassLoader
 * @return Lớp đã được tải
 * @throws IllegalArgumentException Tên lớp rỗng
 * @throws ClassNotFoundException Không tìm thấy lớp
 */
fun loadClass(clzName: String, clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*> {
    if (clzName.isBlank()) throw IllegalArgumentException("Class name must not be null or empty!")
    return clzLoader.loadClass(clzName)
}

/**
 * Thử tải một lớp từ danh sách
 * @param clzName Tên lớp
 * @param clzLoader ClassLoader
 * @return Lớp đầu tiên được tải thành công
 */
fun loadClassAny(
    vararg clzName: String,
    clzLoader: ClassLoader = InitFields.ezXClassLoader
): Class<*> = clzName.firstNotNullOfOrNull { loadClassOrNull(it, clzLoader) }
    ?: throw ClassNotFoundException()

/**
 * Thử tải một lớp từ danh sách, trả về null nếu thất bại
 * @param clzName Tên lớp
 * @param clzLoader ClassLoader
 * @return Lớp đầu tiên được tải thành công hoặc null
 */
fun loadClassAnyOrNull(
    vararg clzName: String,
    clzLoader: ClassLoader = InitFields.ezXClassLoader
): Class<*>? = clzName.firstNotNullOfOrNull { loadClassOrNull(it, clzLoader) }

/**
 * Thử tải một lớp, trả về null nếu thất bại
 * @param clzName Tên lớp
 * @param clzLoader ClassLoader
 * @return Lớp đã được tải
 */
fun loadClassOrNull(
    clzName: String,
    clzLoader: ClassLoader = InitFields.ezXClassLoader
): Class<*>? {
    if (clzName.isBlank()) throw IllegalArgumentException("Class name must not be null or empty!")
    return XposedHelpers.findClassIfExists(clzName, clzLoader)
}

/**
 * Hàm mở rộng để tải tất cả các lớp trong mảng
 * @param clzLoader ClassLoader
 * @return Mảng các lớp
 */
fun Array<String>.loadAllClasses(clzLoader: ClassLoader = InitFields.ezXClassLoader): Array<Class<*>> {
    return Array(this.size) { i -> loadClass(this[i], clzLoader) }
}

fun Iterable<String>.loadAllClasses(clzLoader: ClassLoader = InitFields.ezXClassLoader): List<Class<*>> {
    return this.map { loadClass(it, clzLoader) }
}

/**
 * Hàm mở rộng để thử tải tất cả các lớp trong mảng
 * @param clzLoader ClassLoader
 * @return Mảng các lớp đã tải thành công
 */
fun Array<String>.loadClassesIfExists(clzLoader: ClassLoader = InitFields.ezXClassLoader): Array<Class<*>> {
    return this.mapNotNull { loadClassOrNull(it, clzLoader) }.toTypedArray()
}

fun Iterable<String>.loadClassesIfExists(clzLoader: ClassLoader = InitFields.ezXClassLoader): List<Class<*>> {
    return this.mapNotNull { loadClassOrNull(it, clzLoader) }
}

/**
 * Thử tải một lớp từ mảng
 * @param clzLoader ClassLoader
 * @return Lớp đầu tiên được tải thành công
 */
@JvmName("loadClassAnyFromArray")
fun Array<String>.loadClassAny(clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*> =
    this.firstNotNullOfOrNull { loadClassOrNull(it, clzLoader) } ?: throw ClassNotFoundException()

fun Iterable<String>.loadClassAny(clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*> =
    this.firstNotNullOfOrNull { loadClassOrNull(it, clzLoader) } ?: throw ClassNotFoundException()

/**
 * Thử tải một lớp từ mảng, trả về null nếu thất bại
 * @param clzLoader ClassLoader
 * @return Lớp đầu tiên được tải thành công hoặc null
 */
@JvmName("loadClassAnyOrFromList")
fun Array<String>.loadClassAnyOrNull(clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*>? =
    this.firstNotNullOfOrNull { loadClassOrNull(it, clzLoader) }

fun Iterable<String>.loadClassAnyOrNull(clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*>? =
    this.firstNotNullOfOrNull { loadClassOrNull(it, clzLoader) }

/**
 * Hàm mở rộng để kiểm tra xem lớp hiện tại có phải là lớp con của một lớp khác không
 * @param clzName Tên lớp
 * @param clzLoader ClassLoader
 * @return Có phải là lớp con không
 */
fun Class<*>.isChildClassOf(clzName: String, clzLoader: ClassLoader = InitFields.ezXClassLoader): Boolean =
    loadClass(clzName, clzLoader).isAssignableFrom(this)