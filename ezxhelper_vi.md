# Tài liệu EzXHelper - Các hàm và phương thức hữu ích

## Mục lục
1. [Các hàm tìm kiếm](#các-hàm-tìm-kiếm)
2. [Các hàm đọc/ghi thuộc tính](#các-hàm-đọcghi-thuộc-tính)
3. [Các hàm hook](#các-hàm-hook)
4. [Các hàm làm việc với JSON](#các-hàm-làm-việc-với-json)
5. [Các hàm làm việc với View](#các-hàm-làm-việc-với-view)
6. [Các hàm tiện ích khác](#các-hàm-tiện-ích-khác)

## Các hàm tìm kiếm

### Tìm kiếm trường (Field)
- **findField**: Tìm trường theo điều kiện
  ```kotlin
  fun Class<*>.findField(findSuper: Boolean = false, condition: FieldCondition): Field
  ```
  *Mô tả*: Tìm trường trong một lớp dựa trên điều kiện

- **findFieldOrNull**: Tìm trường theo điều kiện, trả về null nếu không tìm thấy
  ```kotlin
  fun Class<*>.findFieldOrNull(findSuper: Boolean = false, condition: FieldCondition): Field?
  ```

- **findAllFields**: Tìm tất cả các trường thỏa mãn điều kiện
  ```kotlin
  fun Class<*>.findAllFields(findSuper: Boolean = false, condition: FieldCondition): List<Field>
  ```

- **getFieldByDesc**: Lấy trường thông qua mô tả (descriptor)
  ```kotlin
  fun ClassLoader.getFieldByDesc(desc: String): Field
  ```

### Tìm kiếm phương thức (Method)
- **findMethod**: Tìm phương thức theo điều kiện
  ```kotlin
  fun Class<*>.findMethod(findSuper: Boolean = false, condition: MethodCondition): Method
  ```

- **findMethodOrNull**: Tìm phương thức theo điều kiện, trả về null nếu không tìm thấy
  ```kotlin
  fun Class<*>.findMethodOrNull(findSuper: Boolean = false, condition: MethodCondition): Method?
  ```

- **findAllMethods**: Tìm tất cả các phương thức thỏa mãn điều kiện
  ```kotlin
  fun Class<*>.findAllMethods(findSuper: Boolean = false, condition: MethodCondition): List<Method>
  ```

- **getMethodByDesc**: Lấy phương thức thông qua mô tả
  ```kotlin
  fun ClassLoader.getMethodByDesc(desc: String): Method
  ```

### Tìm kiếm constructor
- **findConstructor**: Tìm constructor theo điều kiện
  ```kotlin
  fun Class<*>.findConstructor(condition: ConstructorCondition): Constructor<*>
  ```

- **findConstructorOrNull**: Tìm constructor theo điều kiện, trả về null nếu không tìm thấy
  ```kotlin
  fun Class<*>.findConstructorOrNull(condition: ConstructorCondition): Constructor<*>?
  ```

- **findAllConstructors**: Tìm tất cả các constructor thỏa mãn điều kiện
  ```kotlin
  fun Class<*>.findAllConstructors(condition: ConstructorCondition): List<Constructor<*>>
  ```

### Tìm kiếm lớp (Class)
- **loadClass**: Tải một lớp từ tên
  ```kotlin
  fun loadClass(clzName: String, clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*>
  ```

- **loadClassOrNull**: Tải một lớp từ tên, trả về null nếu không tìm thấy
  ```kotlin
  fun loadClassOrNull(clzName: String, clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*>?
  ```

- **loadClassAny**: Thử tải một lớp từ danh sách tên
  ```kotlin
  fun loadClassAny(vararg clzName: String, clzLoader: ClassLoader = InitFields.ezXClassLoader): Class<*>
  ```

## Các hàm đọc/ghi thuộc tính

### Đọc giá trị
- **getObject**: Lấy đối tượng từ một trường
  ```kotlin
  fun Any.getObject(objName: String, type: Class<*>? = null): Any
  ```

- **getObjectAs**: Lấy đối tượng và chuyển đổi thành kiểu T
  ```kotlin
  fun <T> Any.getObjectAs(objName: String, type: Class<*>? = null): T
  ```

- **getObjectOrNull**: Lấy đối tượng, trả về null nếu không tìm thấy
  ```kotlin
  fun Any.getObjectOrNull(objName: String, type: Class<*>? = null): Any?
  ```

- **getObjectByType**: Lấy đối tượng theo kiểu
  ```kotlin
  fun Any.getObjectByType(type: Class<*>): Any
  ```

### Đọc giá trị tĩnh
- **getStaticObject**: Lấy đối tượng tĩnh từ một lớp
  ```kotlin
  fun Class<*>.getStaticObject(objName: String, type: Class<*>? = null): Any
  ```

- **getStaticObjectAs**: Lấy đối tượng tĩnh và chuyển đổi thành kiểu T
  ```kotlin
  fun <T> Class<*>.getStaticObjectAs(objName: String, type: Class<*>? = null): T
  ```

- **getStaticObjectByType**: Lấy đối tượng tĩnh theo kiểu
  ```kotlin
  fun Class<*>.getStaticObjectByType(type: Class<*>): Any
  ```

### Ghi giá trị
- **putObject**: Đặt giá trị cho một trường
  ```kotlin
  fun Any.putObject(objName: String, value: Any?, fieldType: Class<*>? = null)
  ```

- **putObjectByType**: Đặt giá trị cho một trường theo kiểu
  ```kotlin
  fun Any.putObjectByType(value: Any?, type: Class<*>)
  ```

- **putStaticObject**: Đặt giá trị cho một trường tĩnh
  ```kotlin
  fun Class<*>.putStaticObject(objName: String, value: Any?, fieldType: Class<*>? = null)
  ```

## Các hàm hook

### Hook cơ bản
- **hookBefore**: Hook trước khi phương thức/constructor được thực thi
  ```kotlin
  fun Method.hookBefore(priority: Int = XCallback.PRIORITY_DEFAULT, hook: Hooker): XC_MethodHook.Unhook
  fun Constructor<*>.hookBefore(priority: Int = XCallback.PRIORITY_DEFAULT, hook: Hooker): XC_MethodHook.Unhook
  ```

- **hookAfter**: Hook sau khi phương thức/constructor được thực thi
  ```kotlin
  fun Method.hookAfter(priority: Int = XCallback.PRIORITY_DEFAULT, hooker: Hooker): XC_MethodHook.Unhook
  fun Constructor<*>.hookAfter(priority: Int = XCallback.PRIORITY_DEFAULT, hooker: Hooker): XC_MethodHook.Unhook
  ```

- **hookReplace**: Thay thế phương thức/constructor
  ```kotlin
  fun Method.hookReplace(priority: Int = XCallback.PRIORITY_DEFAULT, hook: ReplaceHooker): XC_MethodHook.Unhook
  fun Constructor<*>.hookReplace(priority: Int = XCallback.PRIORITY_DEFAULT, hooker: ReplaceHooker): XC_MethodHook.Unhook
  ```

### Hook nhiều phương thức
- **hookAllConstructorBefore**: Hook trước tất cả các constructor của một lớp
  ```kotlin
  fun Class<*>.hookAllConstructorBefore(priority: Int = XCallback.PRIORITY_DEFAULT, hooker: Hooker): Array<XC_MethodHook.Unhook>
  ```

- **hookAllConstructorAfter**: Hook sau tất cả các constructor của một lớp
  ```kotlin
  fun Class<*>.hookAllConstructorAfter(priority: Int = XCallback.PRIORITY_DEFAULT, hooker: Hooker): Array<XC_MethodHook.Unhook>
  ```

- **hookAllConstructorReplace**: Thay thế tất cả các constructor của một lớp
  ```kotlin
  fun Class<*>.hookAllConstructorReplace(priority: Int = XCallback.PRIORITY_DEFAULT, hooker: ReplaceHooker): Array<XC_MethodHook.Unhook>
  ```

### Tiện ích hook
- **hookReturnConstant**: Hook phương thức để trả về một giá trị cố định
  ```kotlin
  fun Method.hookReturnConstant(priority: Int = XCallback.PRIORITY_DEFAULT, obj: Any?): XC_MethodHook.Unhook
  ```

- **unhookAll**: Hủy tất cả các hook
  ```kotlin
  fun Array<XC_MethodHook.Unhook>.unhookAll()
  fun Iterable<XC_MethodHook.Unhook>.unhookAll()
  ```

## Các hàm làm việc với JSON

### JSONObject
- **buildJSONObject**: Xây dựng một JSONObject
  ```kotlin
  inline fun buildJSONObject(builder: JSONObject.() -> Unit): JSONObject
  ```

- **getStringOrNull**: Lấy chuỗi từ JSONObject, trả về null nếu không tìm thấy
  ```kotlin
  fun JSONObject.getStringOrNull(key: String): String?
  ```

- **getIntOrNull**: Lấy số nguyên từ JSONObject, trả về null nếu không tìm thấy
  ```kotlin
  fun JSONObject.getIntOrNull(key: String): Int?
  ```

- **getBooleanOrNull**: Lấy giá trị boolean từ JSONObject, trả về null nếu không tìm thấy
  ```kotlin
  fun JSONObject.getBooleanOrNull(key: String): Boolean?
  ```

### JSONArray
- **buildJSONArray**: Xây dựng một JSONArray
  ```kotlin
  inline fun buildJSONArray(builder: JSONArray.() -> Unit): JSONArray
  ```

- **forEach**: Duyệt qua từng phần tử trong JSONArray
  ```kotlin
  inline fun JSONArray.forEach(action: (Any) -> Unit)
  ```

- **filter**: Lọc JSONArray theo điều kiện
  ```kotlin
  inline fun JSONArray.filter(predicate: (Any) -> Boolean): JSONArray
  ```

- **map**: Biến đổi JSONArray
  ```kotlin
  inline fun JSONArray.map(transform: (Any) -> Any): JSONArray
  ```

## Các hàm làm việc với View

### Tìm kiếm View
- **findViewByCondition**: Tìm View theo điều kiện
  ```kotlin
  fun ViewGroup.findViewByCondition(condition: (view: View) -> Boolean): View?
  ```

- **findViewByConditionAs**: Tìm View theo điều kiện và chuyển đổi thành kiểu T
  ```kotlin
  fun <T : View> ViewGroup.findViewByConditionAs(condition: (view: View) -> Boolean): T?
  ```

- **findAllViewsByCondition**: Tìm tất cả các View thỏa mãn điều kiện
  ```kotlin
  fun ViewGroup.findAllViewsByCondition(condition: (view: View) -> Boolean): List<View>
  ```

- **findViewByIdName**: Tìm View theo tên ID
  ```kotlin
  fun Activity.findViewByIdName(name: String): View?
  fun View.findViewByIdName(name: String): View?
  ```

### Thao tác với ViewGroup
- **forEach**: Duyệt qua từng View trong ViewGroup
  ```kotlin
  inline fun ViewGroup.forEach(action: (view: View) -> Unit)
  ```

- **forEachIndexed**: Duyệt qua từng View trong ViewGroup kèm chỉ số
  ```kotlin
  inline fun ViewGroup.forEachIndexed(action: (index: Int, view: View) -> Unit)
  ```

- **isEmpty**: Kiểm tra ViewGroup có rỗng không
  ```kotlin
  fun ViewGroup.isEmpty(): Boolean
  ```

- **isNotEmpty**: Kiểm tra ViewGroup có phần tử không
  ```kotlin
  fun ViewGroup.isNotEmpty(): Boolean
  ```

## Các hàm tiện ích khác

### Xử lý luồng
- **runOnMainThread**: Chạy một đoạn mã trên luồng chính
  ```kotlin
  fun runOnMainThread(runnable: Runnable)
  ```

- **postOnMainThread**: Đăng một Runnable để chạy trên luồng chính
  ```kotlin
  fun Runnable.postOnMainThread()
  ```

### Xử lý ngoại lệ
- **tryOrNull**: Thử thực hiện một đoạn mã, trả về null nếu có lỗi
  ```kotlin
  inline fun <T> tryOrNull(block: () -> T?): T?
  ```

- **tryOrFalse**: Thử thực hiện một đoạn mã, trả về false nếu có lỗi
  ```kotlin
  inline fun tryOrFalse(block: () -> Unit): Boolean
  ```

- **tryOrLog**: Thử thực hiện một đoạn mã, ghi log nếu có lỗi
  ```kotlin
  inline fun tryOrLog(block: () -> Unit)
  ```

### Tiện ích khác
- **showToast**: Hiển thị một Toast
  ```kotlin
  fun Context.showToast(msg: String, length: Int = Toast.LENGTH_SHORT)
  ```

- **restartHostApp**: Khởi động lại ứng dụng chủ
  ```kotlin
  fun restartHostApp(activity: Activity)
  ```

- **addModuleAssetPath**: Thêm đường dẫn tài nguyên của module vào Context
  ```kotlin
  fun Context.addModuleAssetPath()
  ```

- **getIdByName**: Lấy ID từ tên trong R.id
  ```kotlin
  fun getIdByName(name: String, ctx: Context = InitFields.appContext): Int
  ```

- **applyRetainIf**: Giữ lại các phần tử thỏa mãn điều kiện trong danh sách
  ```kotlin
  inline fun <E> MutableList<E>.applyRetainIf(predicate: (E) -> Boolean): MutableList<E>
  ```

- **applyRemoveIf**: Loại bỏ các phần tử thỏa mãn điều kiện trong danh sách
  ```kotlin
  inline fun <K, V> MutableMap<K, V>.applyRemoveIf(predicate: (K, V) -> Boolean): MutableMap<K, V>
  ``` 