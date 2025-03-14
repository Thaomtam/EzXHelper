package com.github.kyuubiran.ezxhelper.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.init.InitFields

/**
 * Hàm mở rộng để đặt chiều cao và chiều rộng của View thành 0
 */
fun View.setViewZeroSize() {
    this.layoutParams.height = 0
    this.layoutParams.width = 0
}

/**
 * Thuộc tính mở rộng để lấy phạm vi độ dài, dùng cho vòng lặp for
 */
inline val ViewGroup.indices: IntRange
    get() = 0 until childCount

/**
 * Hàm mở rộng để duyệt qua ViewGroup
 */
inline fun ViewGroup.forEach(action: (view: View) -> Unit) {
    for (index in this.indices) {
        action(getChildAt(index))
    }
}

/**
 * Hàm mở rộng để duyệt qua ViewGroup kèm theo chỉ số
 */
inline fun ViewGroup.forEachIndexed(action: (index: Int, view: View) -> Unit) {
    for (index in this.indices) {
        action(index, getChildAt(index))
    }
}

/**
 * Hàm mở rộng để kiểm tra ViewGroup có rỗng không
 * @return có rỗng hay không
 */
fun ViewGroup.isEmpty(): Boolean = this.childCount == 0

/**
 * Hàm mở rộng để kiểm tra ViewGroup có không rỗng không
 * @return có không rỗng hay không
 */
fun ViewGroup.isNotEmpty(): Boolean = this.childCount != 0

/**
 * Hàm mở rộng để duyệt qua ViewGroup và tìm View theo điều kiện
 * @param condition điều kiện
 * @return trả về view thỏa mãn điều kiện nếu thành công, trả về null nếu thất bại
 */
fun ViewGroup.findViewByCondition(condition: (view: View) -> Boolean): View? {
    this.forEach {
        if (condition(it)) return it
        else if (it is ViewGroup) {
            val v = it.findViewByCondition(condition)
            if (v != null) return v
        }
    }
    return null
}

/**
 * Hàm mở rộng để duyệt qua ViewGroup và tìm tất cả View thỏa mãn điều kiện
 * @param condition điều kiện
 * @return danh sách các View thỏa mãn điều kiện
 */
fun ViewGroup.findAllViewsByCondition(condition: (view: View) -> Boolean): List<View> {
    val list = mutableListOf<View>()
    this.forEach {
        if (condition(it)) list.add(it)
        else if (it is ViewGroup) {
            val v = it.findAllViewsByCondition(condition)
            if (v.isNotEmpty()) list.addAll(v)
        }
    }
    return list
}

/**
 * Hàm mở rộng để duyệt qua ViewGroup, tìm View theo điều kiện và chuyển đổi View thành kiểu T?
 * @param condition điều kiện
 * @return trả về view thỏa mãn điều kiện nếu thành công, trả về null nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.findViewByConditionAs(condition: (view: View) -> Boolean): T? {
    return this.findViewByCondition(condition) as T?
}

/**
 * Lấy id của thành phần trong R.id thông qua tên
 * @param name tên
 * @return id ID, trả về 0 nếu không tìm thấy
 */
@SuppressLint("DiscouragedApi")
fun getIdByName(name: String, ctx: Context = InitFields.appContext): Int {
    return ctx.resources.getIdentifier(name, "id", ctx.packageName)
}

/**
 * Tìm View thông qua tên
 * @param name tên
 * @return View, trả về null nếu không tìm thấy
 */
fun View.findViewByIdName(name: String): View? {
    val id = getIdByName(name, this.context)
    if (id == 0) return null
    return this.findViewById(id)
}

fun Activity.findViewByIdName(name: String): View? {
    val id = getIdByName(name, this)
    if (id == 0) return null
    return this.findViewById(id)
}
