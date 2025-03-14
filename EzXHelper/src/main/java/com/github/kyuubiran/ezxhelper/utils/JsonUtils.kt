package com.github.kyuubiran.ezxhelper.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Thuộc tính mở rộng để kiểm tra JSONArray có rỗng không
 */
val JSONArray.isEmpty: Boolean
    inline get() = this.length() == 0
val JSONArray.isNotEmpty: Boolean
    inline get() = this.length() != 0

/**
 * Thuộc tính mở rộng để lấy phạm vi độ dài, dùng cho vòng lặp for
 */
val JSONArray.indices: IntRange
    inline get() = 0 until this.length()

/**
 * Hàm mở rộng để duyệt qua JSONArray
 * @param action Thao tác cần thực hiện
 */
inline fun JSONArray.forEach(action: (Any) -> Unit) {
    for (i in this.indices) action(this.get(i))
}

/**
 * Hàm mở rộng để duyệt qua JSONArray kèm theo chỉ số
 * @param action Thao tác cần thực hiện
 */
inline fun JSONArray.forEachIndexed(action: (Int, Any) -> Unit) {
    for (i in this.indices) action(i, this.get(i))
}

/**
 * Hàm mở rộng để duyệt qua JSONArray và trả về cùng một JSONArray
 * @param action Thao tác cần thực hiện
 */
inline fun JSONArray.onEach(action: (Any) -> Unit): JSONArray {
    for (i in this.indices) action(this.get(i))
    return this
}

/**
 * Hàm mở rộng để duyệt qua JSONArray kèm theo chỉ số và trả về cùng một JSONArray
 * @param action Thao tác cần thực hiện
 */
inline fun JSONArray.onEachIndexed(action: (Int, Any) -> Unit): JSONArray {
    for (i in this.indices) action(i, this.get(i))
    return this
}

/**
 * Hàm mở rộng để lọc JSONArray và trả về một JSONArray mới
 * @param predicate Điều kiện lọc
 */
inline fun JSONArray.filter(predicate: (Any) -> Boolean): JSONArray {
    val result = JSONArray()
    for (i in this.indices) if (predicate(this.get(i))) result.put(this.get(i))
    return result
}

/**
 * Hàm mở rộng để chuyển đổi JSONArray và trả về một JSONArray mới
 * @param transform Hàm chuyển đổi
 */
inline fun JSONArray.map(transform: (Any) -> Any): JSONArray {
    val result = JSONArray()
    for (i in this.indices) result.put(transform(this.get(i)))
    return result
}

/**
 * Hàm mở rộng để chuyển đổi JSONArray và trả về một List
 * @param transform Hàm chuyển đổi
 */
inline fun <T> JSONArray.mapToList(transform: (Any) -> T): List<T> {
    val result = ArrayList<T>(this.length())
    for (i in this.indices) result.add(transform(this.get(i)))
    return result
}

/**
 * Hàm mở rộng để lấy giá trị Long từ JSONObject
 * @param key Khóa
 * @param defValue Giá trị mặc định
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về giá trị mặc định
 */
fun JSONObject.getLongOrDefault(key: String, defValue: Long = 0L): Long = try {
    this.getLong(key)
} catch (e: JSONException) {
    defValue
}

/**
 * Hàm mở rộng để lấy giá trị Long từ JSONObject
 * @param key Khóa
 * @param defValue Giá trị mặc định
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về null
 */
fun JSONObject.getLongOrNull(key: String): Long? = try {
    this.getLong(key)
} catch (e: JSONException) {
    null
}

/**
 * Hàm mở rộng để lấy giá trị Int từ JSONObject
 * @param key Khóa
 * @param defValue Giá trị mặc định
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về giá trị mặc định
 */
fun JSONObject.getIntOrDefault(key: String, defValue: Int = 0): Int = try {
    this.getInt(key)
} catch (e: JSONException) {
    defValue
}

/**
 * Hàm mở rộng để lấy giá trị Int từ JSONObject
 * @param key Khóa
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về null
 */
fun JSONObject.getIntOrNull(key: String): Int? = try {
    this.getInt(key)
} catch (e: JSONException) {
    null
}

/**
 * Hàm mở rộng để lấy giá trị Boolean từ JSONObject
 * @param key Khóa
 * @param defValue Giá trị mặc định
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về giá trị mặc định
 */
fun JSONObject.getBooleanOrDefault(key: String, defValue: Boolean = false): Boolean = try {
    this.getBoolean(key)
} catch (e: JSONException) {
    defValue
}

/**
 * Hàm mở rộng để lấy giá trị Boolean từ JSONObject
 * @param key Khóa
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về null
 */
fun JSONObject.getBooleanOrNull(key: String): Boolean? = try {
    this.getBoolean(key)
} catch (e: JSONException) {
    null
}

/**
 * Hàm mở rộng để lấy giá trị String từ JSONObject
 * @param key Khóa
 * @param defValue Giá trị mặc định
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về giá trị mặc định
 */
fun JSONObject.getStringOrDefault(key: String, defValue: String = ""): String = try {
    this.getString(key)
} catch (e: JSONException) {
    defValue
}

/**
 * Hàm mở rộng để lấy giá trị String từ JSONObject
 * @param key Khóa
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về null
 */
fun JSONObject.getStringOrNull(key: String): String? = try {
    this.getString(key)
} catch (e: JSONException) {
    null
}

/**
 * Hàm mở rộng để lấy giá trị Object từ JSONObject
 * @param key Khóa
 * @return Trả về giá trị lấy được nếu thành công, ngược lại trả về null
 */
fun JSONObject.getObjectOrNull(key: String): Any? = try {
    this.get(key)
} catch (e: JSONException) {
    null
}

/**
 * Hàm mở rộng để lấy giá trị JSONArray từ JSONObject
 * @param key Khóa
 * @return Trả về JSONArray nếu thành công, ngược lại trả về JSONArray rỗng
 */
fun JSONObject.getJSONArrayOrEmpty(key: String): JSONArray = try {
    this.getJSONArray(key)
} catch (e: JSONException) {
    JSONArray()
}

/**
 * Hàm mở rộng để lấy giá trị JSONArray từ JSONObject
 * @param key Khóa
 * @return Trả về JSONArray nếu thành công, ngược lại trả về null
 */
fun JSONObject.getJSONArrayOrNull(key: String): JSONArray? = try {
    this.getJSONArray(key)
} catch (e: JSONException) {
    null
}

/**
 * Xây dựng một JSONObject
 */
inline fun buildJSONObject(builder: JSONObject.() -> Unit): JSONObject = JSONObject().apply(builder)

/**
 * Xây dựng một JSONArray
 */
inline fun buildJSONArray(builder: JSONArray.() -> Unit): JSONArray = JSONArray().apply(builder)
