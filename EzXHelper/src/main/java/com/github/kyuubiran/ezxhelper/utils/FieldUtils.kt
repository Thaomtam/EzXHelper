package com.github.kyuubiran.ezxhelper.utils

import com.github.kyuubiran.ezxhelper.init.InitFields
import java.lang.reflect.Field
import java.lang.reflect.Method

typealias FieldCondition = Field.() -> Boolean

/**
 * Tìm thuộc tính trong lớp theo điều kiện
 * @param clz Lớp
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Thuộc tính thỏa mãn điều kiện
 * @throws NoSuchFieldException
 */
fun findField(
    clz: Class<*>,
    findSuper: Boolean = false,
    condition: FieldCondition
): Field {
    return findFieldOrNull(clz, findSuper, condition) ?: throw NoSuchFieldException()
}

/**
 * Tìm thuộc tính trong lớp theo điều kiện
 * @param clz Lớp
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun findFieldOrNull(
    clz: Class<*>,
    findSuper: Boolean = false,
    condition: FieldCondition
): Field? {
    var c = clz
    c.declaredFields.firstOrNull { it.condition() }?.let {
        it.isAccessible = true;return it
    }
    if (findSuper) {
        while (c.superclass?.also { c = it } != null) {
            c.declaredFields.firstOrNull { it.condition() }
                ?.let { it.isAccessible = true;return it }
        }
    }
    return null
}

/**
 * Tìm thuộc tính trong lớp theo điều kiện
 * @param clzName Tên lớp
 * @param classLoader ClassLoader
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Thuộc tính thỏa mãn điều kiện
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun findField(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: FieldCondition
): Field {
    return findField(loadClass(clzName, classLoader), findSuper, condition)
}

/**
 * Tìm thuộc tính trong lớp theo điều kiện
 * @param clzName Tên lớp
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun findFieldOrNull(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: FieldCondition
): Field? {
    return findFieldOrNull(loadClass(clzName, classLoader), findSuper, condition)
}

/**
 * Hàm mở rộng để tìm thuộc tính theo điều kiện
 * @param condition Điều kiện
 * @return Thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun Array<Field>.findField(condition: FieldCondition): Field {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
        ?: throw NoSuchFieldException()
}

fun Iterable<Field>.findField(condition: FieldCondition): Field {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
        ?: throw NoSuchFieldException()
}

/**
 * Hàm mở rộng để tìm thuộc tính theo điều kiện
 * @param condition Điều kiện
 * @return Thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun Array<Field>.findFieldOrNull(condition: FieldCondition): Field? =
    this.firstOrNull { it.condition() }?.apply { isAccessible = true }


fun Iterable<Field>.findFieldOrNull(condition: FieldCondition): Field? =
    this.firstOrNull { it.condition() }?.apply { isAccessible = true }

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy giá trị của nó
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Đối tượng thuộc tính thỏa mãn điều kiện
 * @throws NoSuchFieldException Không tìm thấy thuộc tính phù hợp
 */
fun Any.findFieldObject(findSuper: Boolean = false, condition: FieldCondition): Any =
    this.javaClass.findField(findSuper, condition).get(this)!!

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy giá trị của nó, chuyển đổi thành kiểu T
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Đối tượng thuộc tính thỏa mãn điều kiện
 * @throws NoSuchFieldException Không tìm thấy thuộc tính phù hợp
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.findFieldObjectAs(findSuper: Boolean = false, condition: FieldCondition): T =
    this.javaClass.findField(findSuper, condition).get(this) as T

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy giá trị của nó
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Đối tượng thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun Any.findFieldObjectOrNull(findSuper: Boolean = false, condition: FieldCondition): Any? =
    this.javaClass.findFieldOrNull(findSuper, condition)?.get(this)

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy giá trị của nó, chuyển đổi thành kiểu T?
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Đối tượng thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.findFieldObjectOrNullAs(findSuper: Boolean = false, condition: FieldCondition): T? =
    this.javaClass.findFieldOrNull(findSuper, condition)?.get(this) as T?

/**
 * Lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @param clzLoader ClassLoader
 * @return Thuộc tính đã tìm thấy
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun getFieldByDesc(desc: String, clzLoader: ClassLoader = InitFields.ezXClassLoader): Field =
    DexDescriptor.newFieldDesc(desc).getField(clzLoader).apply { isAccessible = true }

/**
 * Hàm mở rộng để lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @return Thuộc tính đã tìm thấy
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun ClassLoader.getFieldByDesc(desc: String): Field = getFieldByDesc(desc, this)

/**
 * Lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @param clzLoader ClassLoader
 * @return Thuộc tính đã tìm thấy, trả về null nếu không tìm thấy
 */
fun getFieldByDescOrNull(
    desc: String,
    clzLoader: ClassLoader = InitFields.ezXClassLoader
): Field? = runCatching { getFieldByDesc(desc, clzLoader) }.getOrNull()

/**
 * Hàm mở rộng để lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @return Thuộc tính đã tìm thấy, trả về null nếu không tìm thấy
 */
fun ClassLoader.getFieldByDescOrNull(desc: String): Field? = getFieldByDescOrNull(desc, this)

/**
 * Hàm mở rộng để duyệt qua mảng thuộc tính, trả về mảng các thuộc tính thỏa mãn điều kiện
 * @param condition Điều kiện
 * @return Mảng các thuộc tính thỏa mãn điều kiện
 */
fun Array<Field>.findAllFields(condition: FieldCondition): Array<Field> =
    this.filter { it.condition() }.onEach { it.isAccessible = true }.toTypedArray()


fun Iterable<Field>.findAllFields(condition: FieldCondition): List<Field> =
    this.filter { it.condition() }.map { it.isAccessible = true;it }


/**
 * Lấy mảng thuộc tính theo điều kiện
 * @param clz Lớp
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Mảng các thuộc tính thỏa mãn điều kiện
 */
fun findAllFields(
    clz: Class<*>,
    findSuper: Boolean = false,
    condition: FieldCondition
): List<Field> {
    var c = clz
    val arr = ArrayList<Field>()
    arr.addAll(c.declaredFields.findAllFields(condition))
    if (findSuper) {
        while (c.superclass?.also { c = it } != null) {
            arr.addAll(c.declaredFields.findAllFields(condition))
        }
    }
    return arr
}

/**
 * Lấy mảng thuộc tính theo điều kiện
 * @param clzName Tên lớp
 * @param classLoader ClassLoader
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Mảng các thuộc tính thỏa mãn điều kiện
 */
fun findAllFields(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: FieldCondition
): List<Field> = findAllFields(loadClass(clzName, classLoader), findSuper, condition)

/**
 * Hàm mở rộng để lấy một thuộc tính từ lớp hoặc đối tượng
 * @param fieldName Tên thuộc tính
 * @param isStatic Có phải là thuộc tính tĩnh không
 * @param fieldType Kiểu dữ liệu của thuộc tính
 * @return Thuộc tính thỏa mãn điều kiện
 * @throws IllegalArgumentException Tên thuộc tính trống
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun Any.field(
    fieldName: String,
    isStatic: Boolean = false,
    fieldType: Class<*>? = null
): Field {
    if (fieldName.isBlank()) throw IllegalArgumentException("Field name must not be empty!")
    var c: Class<*> = if (this is Class<*>) this else this.javaClass
    do {
        c.declaredFields
            .filter { isStatic == it.isStatic }
            .firstOrNull { (fieldType == null || it.type == fieldType) && (it.name == fieldName) }
            ?.let { it.isAccessible = true;return it }
    } while (c.superclass?.also { c = it } != null)
    throw NoSuchFieldException("Name: $fieldName,Static: $isStatic, Type: ${if (fieldType == null) "ignore" else fieldType.name}")
}

/**
 * Hàm mở rộng để lấy thuộc tính theo kiểu dữ liệu
 * @param type Kiểu dữ liệu
 * @param isStatic Có phải là thuộc tính tĩnh không
 * @return Thuộc tính thỏa mãn điều kiện
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun Any.getFieldByType(type: Class<*>, isStatic: Boolean = false): Field {
    var c: Class<*> = if (this is Class<*>) this else this.javaClass
    do {
        c.declaredFields
            .filter { isStatic == it.isStatic }
            .firstOrNull { it.type == type }
            ?.let { it.isAccessible = true;return it }
    } while (c.superclass?.also { c = it } != null)
    throw NoSuchFieldException()
}

fun Any.getStaticFieldByType(type: Class<*>): Field = this.getFieldByType(type, true)

/**
 * Hàm mở rộng để lấy thuộc tính tĩnh từ lớp
 * @param fieldName Tên thuộc tính
 * @param type Kiểu dữ liệu của thuộc tính
 * @return Thuộc tính thỏa mãn điều kiện
 * @throws IllegalArgumentException Tên thuộc tính trống
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun Class<*>.staticField(fieldName: String, type: Class<*>? = null): Field {
    if (fieldName.isBlank()) throw IllegalArgumentException("Field name must not be empty!")
    return this.field(fieldName, true, type)
}

/**
 * Hàm mở rộng để lấy đối tượng tĩnh và chuyển đổi thành kiểu T?
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Field.getStaticAs(): T? = this.run {
    isAccessible = true
    get(null) as T?
}

/**
 * Hàm mở rộng để lấy đối tượng không null
 * @param obj Đối tượng
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 */
fun Field.getNonNull(obj: Any?): Any = this.run {
    isAccessible = true
    get(obj)!!
}

/**
 * Hàm mở rộng để lấy đối tượng không null và chuyển đổi thành kiểu T
 * @param obj Đối tượng
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Field.getNonNullAs(obj: Any?): T = this.run {
    isAccessible = true
    get(obj)!! as T
}

/**
 * Hàm mở rộng để lấy đối tượng tĩnh không null
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 */
fun Field.getStaticNonNull(): Any = this.run {
    isAccessible = true
    get(null)!!
}

/**
 * Hàm mở rộng để lấy đối tượng tĩnh không null và chuyển đổi thành kiểu T
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Field.getStaticNonNullAs(): T = this.run {
    isAccessible = true
    get(null)!! as T
}

/**
 * Hàm mở rộng để lấy đối tượng và chuyển đổi thành kiểu T?
 * @param obj Đối tượng
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Field.getAs(obj: Any?): T? = this.run {
    isAccessible = true
    get(obj) as T?
}

/**
 * Hàm mở rộng để lấy đối tượng tĩnh
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
fun Field.getStatic(): Any? = this.run {
    isAccessible = true
    get(null)
}

/**
 * Sao chép sâu một đối tượng
 * @param srcObj Đối tượng nguồn
 * @param newObj Đối tượng mới
 * @return Trả về đối tượng đã sao chép nếu thành công, null nếu thất bại
 */
fun <T> fieldCpy(srcObj: T, newObj: T): T? = tryOrLogNull {
    var clz: Class<*> = srcObj!!::class.java
    var fields: Array<Field>
    while (Object::class.java != clz) {
        fields = clz.declaredFields
        for (f in fields) {
            f.isAccessible = true
            f.set(newObj, f.get(srcObj))
        }
        clz = clz.superclass
    }
    newObj
}

typealias ObjectCondition = Any?.() -> Boolean

/**
 * Không khuyến khích sử dụng!! Rất chậm!!
 *
 * Hàm mở rộng để duyệt qua các thuộc tính của đối tượng và trả về đối tượng thỏa mãn điều kiện
 * @param condition Điều kiện
 * @return Trả về đối tượng tìm thấy nếu thành công, null nếu thất bại
 */
fun Any.findObject(condition: ObjectCondition): Any? =
    this.javaClass.declaredFields.firstNotNullOfOrNull {
        it.isAccessible = true
        it.get(this)?.let { o -> o.condition() } ?: false
    }

/**
 * Không khuyến khích sử dụng!! Rất chậm!!
 *
 * Hàm mở rộng để duyệt qua các thuộc tính của đối tượng và trả về đối tượng thỏa mãn điều kiện
 * @param fieldCond Điều kiện thuộc tính
 * @param objCond Điều kiện đối tượng
 * @return Trả về đối tượng tìm thấy nếu thành công, null nếu thất bại
 */
fun Any.findObject(
    fieldCond: FieldCondition,
    objCond: ObjectCondition
): Any? = this.javaClass.declaredFields.firstNotNullOfOrNull f@{
    if (!it.fieldCond()) return@f false
    it.isAccessible = true
    it.get(this)?.let { o -> o.objCond() } ?: false
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Tên đối tượng đích trống
 */
fun Any.getObjectOrNull(objName: String, type: Class<*>? = null): Any? {
    if (objName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    return tryOrLogNull { this.field(objName, fieldType = type).get(this) }
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng
 * @param type Kiểu dữ liệu
 * @param T Kiểu dữ liệu chuyển đổi
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Tên đối tượng đích trống
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectOrNullAs(objName: String, type: Class<*>? = null): T? {
    return this.getObjectOrNull(objName, type) as T?
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Tên đối tượng đích trống
 */
fun Any.getObject(objName: String, type: Class<*>? = null): Any {
    if (objName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    return this.javaClass.field(objName, false, type).get(this)!!
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng
 * @param type Kiểu dữ liệu
 * @param T Kiểu dữ liệu chuyển đổi
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Tên đối tượng đích trống
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectAs(objName: String, type: Class<*>? = null): T =
    this.getObject(objName, type) as T

@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectAs(field: Field): T = field.get(this) as T

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param field Thuộc tính
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
fun Any.getObjectOrNull(field: Field): Any? = tryOrLogNull { field.let { it.isAccessible;it.get(this) } }

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param field Thuộc tính
 * @param T Kiểu dữ liệu chuyển đổi
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectOrNullAs(field: Field): T? = this.getObjectOrNull(field) as T?

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
fun Any.getObjectOrNullByType(type: Class<*>): Any? = tryOrLogNull {
    this.getFieldByType(type).get(this)
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectOrNullByTypeAs(type: Class<*>): T? = this.getObjectOrNullByType(type) as T?

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng cần lấy
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Khi tên đối tượng trống
 */
fun Class<*>.getStaticObjectOrNull(
    objName: String,
    type: Class<*>? = null
): Any? = tryOrLogNull {
    if (objName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    tryOrNull { this.staticField(objName, type) }?.get(null)
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng cần lấy
 * @param type Kiểu dữ liệu
 * @param T Kiểu dữ liệu chuyển đổi
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Khi tên đối tượng trống
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectOrNullAs(
    objName: String,
    type: Class<*>? = null
): T? = this.getStaticObjectOrNull(objName, type) as T?

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng cần lấy
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Khi tên đối tượng trống
 */
fun Class<*>.getStaticObject(
    objName: String,
    type: Class<*>? = null
): Any {
    if (objName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    return this.staticField(objName, type).get(this)!!
}

/**
 * Hàm mở rộng để lấy đối tượng tĩnh từ lớp và chuyển đổi thành kiểu T
 * @param objName Tên đối tượng cần lấy
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 * @throws IllegalArgumentException Khi tên đối tượng trống
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectAs(
    objName: String,
    type: Class<*>? = null
): T = this.getStaticObject(objName, type) as T

/**
 * Lấy đối tượng từ Field
 * @param field Thuộc tính
 * @return Trả về đối tượng đã lấy (Có thể null)
 */
fun getStaticObjectOrNull(field: Field): Any? = field.run {
    isAccessible = true
    get(null)
}

/**
 * Lấy đối tượng từ Field và chuyển đổi thành kiểu T?
 * @param field Thuộc tính
 * @return Trả về đối tượng đã lấy (Có thể null)
 */
@Suppress("UNCHECKED_CAST")
fun <T> getStaticObjectOrNullAs(field: Field): T? = getStaticObjectOrNull(field) as T?

/**
 * Lấy đối tượng từ Field
 * @param field Thuộc tính
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 */
fun getStaticObject(field: Field): Any = field.run {
    isAccessible = true
    get(null)!!
}

/**
 * Lấy đối tượng từ Field và chuyển đổi thành kiểu T
 * @param field Thuộc tính
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> getStaticObjectAs(field: Field): T = getStaticObject(field) as T

/**
 * Hàm mở rộng để lấy đối tượng tĩnh từ lớp thông qua kiểu dữ liệu
 *
 * Không khuyến khích sử dụng, hàm này chỉ trả về đối tượng đầu tiên phù hợp
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
fun Class<*>.getStaticObjectByType(type: Class<*>): Any = this.getStaticFieldByType(type).get(null)!!

/**
 * Hàm mở rộng để lấy đối tượng tĩnh từ lớp thông qua kiểu dữ liệu và chuyển đổi thành kiểu T
 *
 * Không khuyến khích sử dụng, hàm này chỉ trả về đối tượng đầu tiên phù hợp
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, ném ngoại lệ nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectByTypeAs(type: Class<*>): T = this.getStaticFieldByType(type).get(null) as T

/**
 * Hàm mở rộng để lấy đối tượng tĩnh từ lớp thông qua kiểu dữ liệu
 *
 * Không khuyến khích sử dụng, hàm này chỉ trả về đối tượng đầu tiên phù hợp
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
fun Class<*>.getStaticObjectOrNullByType(type: Class<*>): Any? = tryOrLogNull {
    this.getStaticFieldByType(type).get(null)
}

/**
 * Hàm mở rộng để lấy đối tượng tĩnh từ lớp thông qua kiểu dữ liệu và chuyển đổi thành kiểu T?
 *
 * Không khuyến khích sử dụng, hàm này chỉ trả về đối tượng đầu tiên phù hợp
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectOrNullByTypeAs(type: Class<*>): T? = this.getStaticFieldByType(type) as T?

/**
 * Hàm mở rộng để đặt giá trị cho thuộc tính của đối tượng
 *
 * @param objName Tên thuộc tính cần đặt giá trị
 * @param value Giá trị
 * @param fieldType Kiểu dữ liệu của thuộc tính
 * @throws IllegalArgumentException Tên thuộc tính trống
 */
fun Any.putObject(objName: String, value: Any?, fieldType: Class<*>? = null) {
    if (objName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    tryOrLog { this.field(objName, false, fieldType).set(this, value) }
}

/**
 * Hàm mở rộng để đặt giá trị cho thuộc tính của đối tượng
 * @param field Thuộc tính
 * @param value Giá trị
 */
fun Any.putObject(field: Field, value: Any?) = tryOrLog {
    field.let {
        it.isAccessible = true
        it.set(this, value)
    }
}

/**
 * Hàm mở rộng để đặt giá trị cho thuộc tính của đối tượng thông qua kiểu dữ liệu
 *
 * Không khuyến khích sử dụng, chỉ đặt giá trị cho thuộc tính đầu tiên có kiểu dữ liệu phù hợp
 * @param value Giá trị
 * @param type Kiểu dữ liệu
 */
fun Any.putObjectByType(value: Any?, type: Class<*>) = tryOrLog {
    this.getFieldByType(type).set(this, value)
}

/**
 * Hàm mở rộng để đặt giá trị cho thuộc tính tĩnh của lớp thông qua kiểu dữ liệu
 *
 * Không khuyến khích sử dụng, chỉ đặt giá trị cho thuộc tính đầu tiên có kiểu dữ liệu phù hợp
 * @param value Giá trị
 * @param type Kiểu dữ liệu
 */
fun Class<*>.putStaticObjectByType(value: Any?, type: Class<*>) = tryOrLog {
    this.getStaticFieldByType(type).set(null, value)
}

/**
 * Hàm mở rộng để đặt giá trị cho thuộc tính tĩnh của lớp
 * @param objName Tên thuộc tính cần đặt giá trị
 * @param value Giá trị
 * @param fieldType Kiểu dữ liệu của thuộc tính
 * @throws IllegalArgumentException Tên thuộc tính trống
 */
fun Class<*>.putStaticObject(objName: String, value: Any?, fieldType: Class<*>? = null) = tryOrLog {
    if (objName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    try {
        this.staticField(objName, fieldType)
    } catch (e: NoSuchFieldException) {
        return
    }.set(null, value)
}

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy đối tượng
 * @param findSuper Có tìm kiếm trong lớp cha hay không
 * @param condition Điều kiện
 * @return Đối tượng của thuộc tính thỏa mãn điều kiện
 * @throws NoSuchFieldException Không tìm thấy thuộc tính phù hợp
 */
fun Any.findFieldObject(findSuper: Boolean = false, condition: FieldCondition): Any =
    this.javaClass.findField(findSuper, condition).get(this)!!

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy đối tượng, chuyển đổi thành kiểu T
 * @param findSuper Có tìm kiếm trong lớp cha hay không
 * @param condition Điều kiện
 * @return Đối tượng của thuộc tính thỏa mãn điều kiện
 * @throws NoSuchFieldException Không tìm thấy thuộc tính phù hợp
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.findFieldObjectAs(findSuper: Boolean = false, condition: FieldCondition): T =
    this.javaClass.findField(findSuper, condition).get(this) as T

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy đối tượng
 * @param findSuper Có tìm kiếm trong lớp cha hay không
 * @param condition Điều kiện
 * @return Đối tượng của thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun Any.findFieldObjectOrNull(findSuper: Boolean = false, condition: FieldCondition): Any? =
    this.javaClass.findFieldOrNull(findSuper, condition)?.get(this)

/**
 * Hàm mở rộng để tìm thuộc tính thỏa mãn điều kiện và lấy đối tượng, chuyển đổi thành kiểu T?
 * @param findSuper Có tìm kiếm trong lớp cha hay không
 * @param condition Điều kiện
 * @return Đối tượng của thuộc tính thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.findFieldObjectOrNullAs(findSuper: Boolean = false, condition: FieldCondition): T? =
    this.javaClass.findFieldOrNull(findSuper, condition)?.get(this) as T?

/**
 * Lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @param clzLoader Bộ tải lớp
 * @return Thuộc tính tìm thấy
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun getFieldByDesc(desc: String, clzLoader: ClassLoader = InitFields.ezXClassLoader): Field =
    DexDescriptor.newFieldDesc(desc).getField(clzLoader).apply { isAccessible = true }

/**
 * Hàm mở rộng để lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @return Thuộc tính tìm thấy
 * @throws NoSuchFieldException Không tìm thấy thuộc tính
 */
fun ClassLoader.getFieldByDesc(desc: String): Field = getFieldByDesc(desc, this)

/**
 * Lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @param clzLoader Bộ tải lớp
 * @return Thuộc tính tìm thấy, trả về null nếu không tìm thấy
 */
fun getFieldByDescOrNull(
    desc: String,
    clzLoader: ClassLoader = InitFields.ezXClassLoader
): Field? = runCatching { getFieldByDesc(desc, clzLoader) }.getOrNull()

/**
 * Hàm mở rộng để lấy thuộc tính thông qua Descriptor
 * @param desc Descriptor
 * @return Thuộc tính tìm thấy, trả về null nếu không tìm thấy
 */
fun ClassLoader.getFieldByDescOrNull(desc: String): Field? = getFieldByDescOrNull(desc, this)

/**
 * Không khuyến khích sử dụng!! Rất chậm!!
 *
 * Hàm mở rộng để duyệt qua các thuộc tính tĩnh của lớp và trả về đối tượng tĩnh thỏa mãn điều kiện
 * @param condition Điều kiện
 * @return Trả về đối tượng tĩnh tìm thấy nếu thành công, null nếu thất bại
 */
fun Class<*>.findStaticObject(condition: ObjectCondition): Any? =
    this.declaredFields.firstNotNullOfOrNull {
        it.isAccessible = true
        it.get(null)?.let(condition) ?: false
    }

/**
 * Không khuyến khích sử dụng!! Rất chậm!!
 *
 * Hàm mở rộng để duyệt qua các thuộc tính tĩnh của lớp và trả về đối tượng tĩnh thỏa mãn điều kiện
 * @param fieldCond Điều kiện thuộc tính
 * @param objCond Điều kiện đối tượng
 * @return Trả về đối tượng tĩnh tìm thấy nếu thành công, null nếu thất bại
 */
fun Any.findStaticObject(
    fieldCond: FieldCondition,
    objCond: ObjectCondition
): Any? = this.javaClass.declaredFields.firstNotNullOfOrNull f@{
    if (!it.fieldCond()) return@f false
    it.isAccessible = true
    it.get(null)?.let(objCond) ?: false
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng
 * @param type Kiểu dữ liệu
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Tên đối tượng đích trống
 */
fun Any.getObject(objName: String, type: Class<*>? = null): Any? {
    if (objName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    return this.field(objName, false, type).getAs(this)
}

/**
 * Hàm mở rộng để lấy đối tượng từ đối tượng đã khởi tạo
 * @param objName Tên đối tượng
 * @param type Kiểu dữ liệu
 * @param T Kiểu dữ liệu chuyển đổi
 * @return Trả về đối tượng đã lấy nếu thành công, null nếu thất bại
 * @throws IllegalArgumentException Tên đối tượng đích trống
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectOrNullAs(objName: String, type: Class<*>? = null): T? {
    return this.getObjectOrNull(objName, type) as T?
}
