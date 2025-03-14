package com.github.kyuubiran.ezxhelper.utils

import com.github.kyuubiran.ezxhelper.init.InitFields
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

@JvmInline
value class Args(val args: Array<out Any?>)

@JvmInline
value class ArgTypes(val argTypes: Array<out Class<*>>)

@Suppress("NOTHING_TO_INLINE")
inline fun args(vararg args: Any?) = Args(args)

@Suppress("NOTHING_TO_INLINE")
inline fun argTypes(vararg argTypes: Class<*>) = ArgTypes(argTypes)

/**
 * Hàm mở rộng để lấy một phương thức từ lớp hoặc đối tượng
 * @param methodName Tên phương thức
 * @param isStatic Có phải là phương thức tĩnh không
 * @param returnType Kiểu trả về của phương thức, điền null để bỏ qua kiểu trả về
 * @param argTypes Kiểu tham số của phương thức
 * @return Phương thức thỏa mãn điều kiện
 * @throws IllegalArgumentException Tên phương thức rỗng
 * @throws NoSuchMethodException Không tìm thấy phương thức
 */
fun Any.method(
    methodName: String,
    returnType: Class<*>? = null,
    isStatic: Boolean = false,
    argTypes: ArgTypes = argTypes()
): Method {
    if (methodName.isBlank()) throw IllegalArgumentException("Method name must not be empty!")
    var c = if (this is Class<*>) this else this.javaClass
    do {
        c.declaredMethods.toList().asSequence()
            .filter { it.name == methodName }
            .filter { it.parameterTypes.size == argTypes.argTypes.size }
            .apply { if (returnType != null) filter { returnType == it.returnType } }
            .filter { it.parameterTypes.sameAs(*argTypes.argTypes) }
            .filter { it.isStatic == isStatic }
            .firstOrNull()?.let { it.isAccessible = true; return it }
    } while (c.superclass?.also { c = it } != null)
    throw NoSuchMethodException("Name:$methodName, Static: $isStatic, ArgTypes:${argTypes.argTypes.joinToString(",")}")
}

/**
 * Hàm mở rộng để lấy một phương thức tĩnh từ lớp
 * @param methodName Tên phương thức
 * @param returnType Kiểu trả về của phương thức, điền null để bỏ qua kiểu trả về
 * @param argTypes Kiểu tham số của phương thức
 * @throws IllegalArgumentException Tên phương thức rỗng
 */
fun Class<*>.staticMethod(
    methodName: String,
    returnType: Class<*>? = null,
    argTypes: ArgTypes = argTypes()
): Method {
    if (methodName.isBlank()) throw IllegalArgumentException("Method name must not be empty!")
    return this.method(methodName, returnType, true, argTypes = argTypes)
}

typealias MethodCondition = Method.() -> Boolean

/**
 * Tìm phương thức trong lớp theo điều kiện
 * @param clz Lớp
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Phương thức thỏa mãn điều kiện
 * @throws NoSuchMethodException
 */
fun findMethod(
    clz: Class<*>,
    findSuper: Boolean = false,
    condition: MethodCondition
): Method {
    return findMethodOrNull(clz, findSuper, condition) ?: throw NoSuchMethodException()
}

/**
 * Tìm phương thức trong lớp theo điều kiện
 * @param clz Lớp
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Phương thức thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun findMethodOrNull(
    clz: Class<*>,
    findSuper: Boolean = false,
    condition: MethodCondition
): Method? {
    var c = clz
    c.declaredMethods.firstOrNull { it.condition() }
        ?.let { it.isAccessible = true;return it }

    if (findSuper) {
        while (c.superclass?.also { c = it } != null) {
            c.declaredMethods
                .firstOrNull { it.condition() }
                ?.let { it.isAccessible = true;return it }
        }
    }
    return null
}

/**
 * Tìm phương thức theo điều kiện
 * @param clzName Tên lớp
 * @param classLoader ClassLoader
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Phương thức thỏa mãn điều kiện
 * @throws NoSuchMethodException Không tìm thấy phương thức
 */
fun findMethod(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: MethodCondition
): Method {
    return findMethod(loadClass(clzName, classLoader), findSuper, condition)
}

/**
 * Hàm mở rộng để tìm phương thức theo điều kiện
 * @param condition Điều kiện của phương thức
 * @return Phương thức thỏa mãn điều kiện
 * @throws NoSuchMethodException Không tìm thấy phương thức
 */
fun Array<Method>.findMethod(condition: MethodCondition): Method {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
        ?: throw NoSuchMethodException()
}

fun Iterable<Method>.findMethod(condition: MethodCondition): Method {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
        ?: throw NoSuchMethodException()
}

/**
 * Hàm mở rộng để tìm phương thức theo điều kiện
 * @param condition Điều kiện của phương thức
 * @return Phương thức thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun Array<Method>.findMethodOrNull(condition: MethodCondition): Method? {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
}

fun Iterable<Method>.findMethodOrNull(condition: MethodCondition): Method? {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
}

/**
 * Hàm mở rộng để tìm phương thức theo điều kiện, mỗi lớp chỉ tìm một phương thức
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện của phương thức
 * @return Mảng phương thức
 */
fun Array<Class<*>>.findMethods(
    findSuper: Boolean = false,
    condition: MethodCondition
): Array<Method> = mapNotNull { it.findMethodOrNull(findSuper, condition) }.toTypedArray()

fun Iterable<Class<*>>.findMethods(
    findSuper: Boolean = false,
    condition: MethodCondition
): List<Method> = mapNotNull { it.findMethodOrNull(findSuper, condition) }

/**
 * Hàm mở rộng để tải các lớp trong mảng và tìm phương thức theo điều kiện, mỗi lớp chỉ tìm một phương thức
 * @param classLoader ClassLoader
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện của phương thức
 * @return Mảng phương thức
 */
fun Array<String>.loadAndFindMethods(
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: MethodCondition
): Array<Method> {
    return this.loadAllClasses(classLoader).findMethods(findSuper, condition)
}

fun Iterable<String>.loadAndFindMethods(
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: MethodCondition
): List<Method> {
    return this.loadAllClasses(classLoader).findMethods(findSuper, condition)
}

// Method condition pair
infix fun String.mcp(condition: MethodCondition) = this to condition
infix fun Class<*>.mcp(condition: MethodCondition) = this to condition

/**
 * Hàm mở rộng để tìm phương thức tương ứng trong mảng theo điều kiện, mỗi lớp chỉ tìm một phương thức
 * @param findSuper Có tìm trong lớp cha không
 * @return Mảng phương thức
 */
fun Array<Pair<Class<*>, MethodCondition>>.findMethods(
    findSuper: Boolean = false
): Array<Method> {
    return this.map { (k, v) -> findMethod(k, findSuper, v) }.toTypedArray()
}

/**
 * Hàm mở rộng để tìm tất cả phương thức theo điều kiện
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện của phương thức
 * @return Mảng phương thức
 */
fun Array<Class<*>>.findAllMethods(
    findSuper: Boolean = false,
    condition: MethodCondition
): Array<Method> {
    return this.flatMap { c -> findAllMethods(c, findSuper, condition) }.toTypedArray()
}

fun Iterable<Class<*>>.findAllMethods(
    findSuper: Boolean = false,
    condition: MethodCondition
): List<Method> {
    return this.flatMap { c -> findAllMethods(c, findSuper, condition) }
}

/**
 * Hàm mở rộng để tìm tất cả phương thức trong mảng theo điều kiện
 * @param findSuper Có tìm trong lớp cha không
 * @return Mảng phương thức
 */
fun Array<Pair<Class<*>, MethodCondition>>.findAllMethods(
    findSuper: Boolean = false
): Array<Method> {
    return arrayListOf<Method>()
        .apply { this@findAllMethods.forEach { (k, v) -> addAll(findAllMethods(k, findSuper, v)) } }
        .toTypedArray()
}

fun Iterable<Pair<Class<*>, MethodCondition>>.findAllMethods(
    findSuper: Boolean = false
): List<Method> {
    return arrayListOf<Method>()
        .apply { this@findAllMethods.forEach { (k, v) -> addAll(findAllMethods(k, findSuper, v)) } }
}

/**
 * Hàm mở rộng để tải các lớp trong mảng và tìm tất cả phương thức theo điều kiện
 * @param classLoader ClassLoader
 * @param findSuper Có tìm trong lớp cha không
 * @return Mảng phương thức
 */
fun Array<Pair<String, MethodCondition>>.loadAndFindAllMethods(
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false
): Array<Method> {
    return this.map { (k, v) -> loadClass(k, classLoader) to v }.toTypedArray()
        .findAllMethods(findSuper)
}

fun Iterable<Pair<String, MethodCondition>>.loadAndFindAllMethods(
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false
): List<Method> {
    return this.map { (k, v) -> loadClass(k, classLoader) to v }.findAllMethods(findSuper)
}

/**
 * Hàm mở rộng để tìm tất cả phương thức theo điều kiện
 * @param classLoader ClassLoader
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện của phương thức
 * @return Mảng phương thức
 */
fun Array<String>.loadAndFindAllMethods(
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: MethodCondition
): Array<Method> {
    return this.loadAllClasses(classLoader).findAllMethods(findSuper, condition)
}

fun Iterable<String>.loadAndFindAllMethods(
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: MethodCondition
): List<Method> {
    return this.loadAllClasses(classLoader).findAllMethods(findSuper, condition)
}

typealias ConstructorCondition = Constructor<*>.() -> Boolean

/**
 * Hàm mở rộng để tìm constructor theo điều kiện
 * @param condition Điều kiện của constructor
 * @return Constructor thỏa mãn điều kiện
 * @throws NoSuchMethodException Không tìm thấy constructor
 */
fun Array<Constructor<*>>.findConstructor(condition: ConstructorCondition): Constructor<*> {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
        ?: throw NoSuchMethodException()
}

fun Iterable<Constructor<*>>.findConstructor(condition: ConstructorCondition): Constructor<*> {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
        ?: throw NoSuchMethodException()
}

/**
 * Hàm mở rộng để tìm constructor theo điều kiện
 * @param condition Điều kiện của constructor
 * @return Constructor thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun Array<Constructor<*>>.findConstructorOrNull(condition: ConstructorCondition): Constructor<*>? {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
}

fun Iterable<Constructor<*>>.findConstructorOrNull(condition: ConstructorCondition): Constructor<*>? {
    return this.firstOrNull { it.condition() }?.apply { isAccessible = true }
}

/**
 * Tìm constructor theo điều kiện
 * @param clz Lớp
 * @param condition Điều kiện
 * @return Constructor thỏa mãn điều kiện
 * @throws NoSuchMethodException Không tìm thấy constructor
 */
fun findConstructor(
    clz: Class<*>,
    condition: ConstructorCondition
): Constructor<*> {
    return clz.declaredConstructors.findConstructor(condition)
}

/**
 * Tìm constructor theo điều kiện
 * @param clz Lớp
 * @param condition Điều kiện
 * @return Constructor thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun findConstructorOrNull(
    clz: Class<*>,
    condition: ConstructorCondition
): Constructor<*>? {
    return clz.declaredConstructors.firstOrNull { it.condition() }?.apply { isAccessible = true }
}

/**
 * Tìm constructor theo điều kiện
 * @param clzName Lớp
 * @param classLoader ClassLoader
 * @param condition Điều kiện
 * @return Constructor thỏa mãn điều kiện
 * @throws NoSuchMethodException Không tìm thấy constructor
 */
fun findConstructor(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    condition: ConstructorCondition
): Constructor<*> {
    return loadClass(clzName, classLoader).declaredConstructors.findConstructor(condition)
}

/**
 * Tìm constructor theo điều kiện
 * @param clzName Lớp
 * @param classLoader ClassLoader
 * @param condition Điều kiện
 * @return Constructor thỏa mãn điều kiện, trả về null nếu không tìm thấy
 */
fun findConstructorOrNull(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    condition: ConstructorCondition
): Constructor<*>? {
    return loadClass(clzName, classLoader).declaredConstructors.findConstructorOrNull(condition)
}

/**
 * Tìm tất cả constructor theo điều kiện
 * @param clzName Lớp
 * @param classLoader ClassLoader
 * @param condition Điều kiện
 * @return Tất cả constructor thỏa mãn điều kiện
 */
fun findAllConstructors(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    condition: ConstructorCondition
): List<Constructor<*>> {
    return loadClass(clzName, classLoader).declaredConstructors.filter(condition)
}

/**
 * Tìm tất cả constructor theo điều kiện
 * @param clz Lớp
 * @param condition Điều kiện
 * @return Tất cả constructor thỏa mãn điều kiện
 */
fun findAllConstructors(
    clz: Class<*>,
    condition: ConstructorCondition
): List<Constructor<*>> {
    return clz.declaredConstructors.filter(condition)
}

/**
 * Hàm mở rộng để duyệt qua mảng phương thức và trả về mảng phương thức thỏa mãn điều kiện
 * @param condition Điều kiện
 * @return Mảng phương thức thỏa mãn điều kiện
 */
fun Array<Method>.findAllMethods(condition: MethodCondition): Array<Method> {
    return this.filter { it.condition() }.onEach { it.isAccessible = true }.toTypedArray()
}

fun Iterable<Method>.findAllMethods(condition: MethodCondition): List<Method> {
    return this.filter { it.condition() }.onEach { it.isAccessible = true }.toList()
}

/**
 * Lấy mảng phương thức theo điều kiện
 * @param clz Lớp
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Mảng phương thức thỏa mãn điều kiện
 */
fun findAllMethods(
    clz: Class<*>,
    findSuper: Boolean = false,
    condition: MethodCondition
): List<Method> {
    var c = clz
    val arr = ArrayList<Method>()
    arr.addAll(c.declaredMethods.findAllMethods(condition))
    if (findSuper) {
        while (c.superclass?.also { c = it } != null) {
            arr.addAll(c.declaredMethods.findAllMethods(condition))
        }
    }
    return arr
}

/**
 * Lấy mảng phương thức theo điều kiện
 * @param clzName Tên lớp
 * @param classLoader ClassLoader
 * @param findSuper Có tìm trong lớp cha không
 * @param condition Điều kiện
 * @return Mảng phương thức thỏa mãn điều kiện
 */
fun findAllMethods(
    clzName: String,
    classLoader: ClassLoader = InitFields.ezXClassLoader,
    findSuper: Boolean = false,
    condition: MethodCondition
): List<Method> {
    return findAllMethods(loadClass(clzName, classLoader), findSuper, condition)
}

/**
 * Hàm mở rộng để gọi phương thức trong đối tượng thỏa mãn điều kiện
 * @param args Tham số
 * @param condition Điều kiện
 * @return Giá trị trả về của phương thức
 * @throws NoSuchMethodException Không tìm thấy phương thức
 */
fun Any.invokeMethod(vararg args: Any?, condition: MethodCondition): Any? {
    this::class.java.declaredMethods.firstOrNull { it.condition() }
        ?.let { it.isAccessible = true;return it(this, *args) }
    throw NoSuchMethodException()
}

/**
 * Hàm mở rộng để gọi phương thức tĩnh trong lớp thỏa mãn điều kiện
 * @param args Danh sách tham số
 * @param condition Điều kiện
 * @return Giá trị trả về của phương thức
 * @throws NoSuchMethodException Không tìm thấy phương thức
 */
fun Class<*>.invokeStaticMethod(
    vararg args: Any?,
    condition: MethodCondition
): Any? {
    this::class.java.declaredMethods.firstOrNull { it.isStatic && it.condition() }
        ?.let { it.isAccessible = true;return it(this, *args) }
    throw NoSuchMethodException()
}

/**
 * Hàm mở rộng để gọi phương thức của đối tượng
 *
 * @param methodName Tên phương thức
 * @param args Danh sách tham số, có thể null
 * @param argTypes Kiểu tham số, có thể null
 * @param returnType Kiểu trả về, có thể null
 * @return Giá trị trả về của phương thức
 * @throws IllegalArgumentException Khi tên phương thức rỗng
 * @throws IllegalArgumentException Khi độ dài của args không khớp với độ dài của argTypes
 * @throws IllegalArgumentException Khi đối tượng là một Class
 */
fun Any.invokeMethod(
    methodName: String,
    args: Args = args(),
    argTypes: ArgTypes = argTypes(),
    returnType: Class<*>? = null
): Any? {
    if (methodName.isBlank()) throw IllegalArgumentException("Object name must not be empty!")
    if (args.args.size != argTypes.argTypes.size) throw IllegalArgumentException("Method args size must equals argTypes size!")
    return if (args.args.isEmpty()) {
        try {
            this.method(methodName, returnType, false)
        } catch (e: NoSuchMethodException) {
            return null
        }.invoke(this)
    } else {
        try {
            this.method(methodName, returnType, false, argTypes = argTypes)
        } catch (e: NoSuchMethodException) {
            return null
        }.invoke(this, *args.args)
    }
}

/**
 * Hàm mở rộng để gọi phương thức của đối tượng và chuyển đổi giá trị trả về thành kiểu T?
 *
 * Lưu ý: Không sử dụng hàm này với lớp
 * @param methodName Tên phương thức
 * @param args Danh sách tham số, có thể null
 * @param argTypes Kiểu tham số, có thể null
 * @param returnType Kiểu trả về, null nếu bỏ qua kiểu trả về
 * @param T Kiểu chuyển đổi
 * @return Giá trị trả về của phương thức
 * @throws IllegalArgumentException Khi tên phương thức rỗng
 * @throws IllegalArgumentException Khi độ dài của args không khớp với độ dài của argTypes
 * @throws IllegalArgumentException Khi đối tượng là một Class
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.invokeMethodAs(
    methodName: String,
    args: Args = args(),
    argTypes: ArgTypes = argTypes(),
    returnType: Class<*>? = null
): T? = this.invokeMethod(methodName, args, argTypes, returnType) as T?

/**
 * Hàm mở rộng để gọi phương thức phù hợp nhất với danh sách tham số của đối tượng
 * @param methodName Tên phương thức
 * @param args Tham số
 * @return Giá trị trả về của phương thức
 */
fun Any.invokeMethodAuto(
    methodName: String,
    vararg args: Any?
): Any? {
    return XposedHelpers.callMethod(this, methodName, *args)
}

/**
 * Hàm mở rộng để gọi phương thức phù hợp nhất với danh sách tham số của đối tượng và chuyển đổi giá trị trả về thành kiểu T?
 * @param methodName Tên phương thức
 * @param args Tham số
 * @return Giá trị trả về của phương thức
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.invokeMethodAutoAs(
    methodName: String,
    vararg args: Any?
): T? = XposedHelpers.callMethod(this, methodName, *args) as T?

/**
 * Hàm mở rộng để gọi phương thức tĩnh của lớp
 * @param methodName Tên phương thức
 * @param args Danh sách tham số
 * @param argTypes Kiểu tham số
 * @param returnType Kiểu trả về
 * @return Giá trị trả về của phương thức
 * @throws IllegalArgumentException Khi độ dài của args không khớp với độ dài của argTypes
 */
fun Class<*>.invokeStaticMethod(
    methodName: String,
    args: Args = args(),
    argTypes: ArgTypes = argTypes(),
    returnType: Class<*>? = null
): Any? {
    if (args.args.size != argTypes.argTypes.size) throw IllegalArgumentException("Method args size must equals argTypes size!")
    return if (args.args.isEmpty()) {
        try {
            this.method(methodName, returnType, true)
        } catch (e: NoSuchMethodException) {
            return null
        }.invoke(this)
    } else {
        try {
            this.method(methodName, returnType, true, argTypes = argTypes)
        } catch (e: NoSuchMethodException) {
            return null
        }.invoke(this, *args.args)
    }
}

/**
 * Hàm mở rộng để gọi phương thức tĩnh của lớp và chuyển đổi giá trị trả về thành kiểu T?
 * @param methodName Tên phương thức
 * @param args Danh sách tham số
 * @param argTypes Kiểu tham số
 * @param returnType Kiểu trả về
 * @return Giá trị trả về của phương thức
 * @throws IllegalArgumentException Khi độ dài của args không khớp với độ dài của argTypes
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.invokeStaticMethodAs(
    methodName: String,
    args: Args = args(),
    argTypes: ArgTypes = argTypes(),
    returnType: Class<*>? = null
): T? = this.invokeStaticMethod(methodName, args, argTypes, returnType) as T?

/**
 * Hàm mở rộng để gọi phương thức tĩnh của lớp
 * @param methodName Tên phương thức
 * @param args Danh sách tham số
 * @return Giá trị trả về của phương thức
 */
fun Class<*>.invokeStaticMethodAuto(
    methodName: String,
    vararg args: Any?
): Any? = XposedHelpers.callStaticMethod(this, methodName, *args)

/**
 * Hàm mở rộng để gọi phương thức tĩnh của lớp và chuyển đổi giá trị trả về thành kiểu T?
 * @param methodName Tên phương thức
 * @param args Danh sách tham số
 * @return Giá trị trả về của phương thức
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.invokeStaticMethodAutoAs(
    methodName: String,
    vararg args: Any?
): T? = XposedHelpers.callStaticMethod(this, methodName, *args) as T?

/**
 * Hàm mở rộng để tạo mới một đối tượng đã khởi tạo
 * @param args Danh sách tham số của constructor
 * @param argTypes Kiểu tham số của constructor
 * @return Trả về đối tượng đã khởi tạo thành công, trả về null nếu không thành công
 * @throws IllegalArgumentException Khi độ dài của args không khớp với độ dài của argTypes
 */
fun Class<*>.newInstance(
    args: Args = args(),
    argTypes: ArgTypes = argTypes()
): Any? {
    if (args.args.size != argTypes.argTypes.size) throw IllegalArgumentException("Method args size must equals argTypes size!")
    return tryOrLogNull {
        val constructor: Constructor<*> =
            if (argTypes.argTypes.isNotEmpty())
                this.getDeclaredConstructor(*argTypes.argTypes)
            else
                this.getDeclaredConstructor()
        constructor.isAccessible = true

        if (args.args.isEmpty())
            constructor.newInstance()
        else
            constructor.newInstance(*args.args)
    }
}

/**
 * Hàm mở rộng để tạo mới một đối tượng đã khởi tạo và chuyển đổi đối tượng thành kiểu T?
 * @param args Danh sách tham số của constructor
 * @param argTypes Kiểu tham số của constructor
 * @return Trả về đối tượng đã khởi tạo thành công, trả về null nếu không thành công
 * @throws IllegalArgumentException Khi độ dài của args không khớp với độ dài của argTypes
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.newInstanceAs(
    args: Args = args(),
    argTypes: ArgTypes = argTypes()
): T? = this.newInstance(args, argTypes) as T?

/**
 * Hàm mở rộng để gọi phương thức và chuyển đổi giá trị trả về thành kiểu T?
 * @param obj Đối tượng được gọi
 * @param args Danh sách tham số
 */
@Suppress("UNCHECKED_CAST")
fun <T> Method.invokeAs(obj: Any?, vararg args: Any?): T? = this.run {
    isAccessible = true
    invoke(obj, *args) as T?
}

/**
 * Lấy phương thức thông qua Descriptor
 * @param desc Descriptor
 * @param clzLoader ClassLoader
 * @return Phương thức tìm thấy
 * @throws NoSuchMethodException Không tìm thấy phương thức
 */
fun getMethodByDesc(
    desc: String,
    clzLoader: ClassLoader = InitFields.ezXClassLoader
): Method = DexDescriptor.newMethodDesc(desc).getMethod(clzLoader).apply { isAccessible = true }

/**
 * Lấy phương thức thông qua Descriptor
 * @param desc Descriptor
 * @param clzLoader ClassLoader
 * @return Phương thức tìm thấy, trả về null nếu không tìm thấy
 */
fun getMethodByDescOrNull(
    desc: String,
    clzLoader: ClassLoader = InitFields.ezXClassLoader
): Method? = runCatching { getMethodByDesc(desc, clzLoader) }.getOrNull()


/**
 * Hàm mở rộng để lấy phương thức thông qua Descriptor
 * @param desc Descriptor
 * @return Phương thức tìm thấy
 * @throws NoSuchMethodException Không tìm thấy phương thức
 */
fun ClassLoader.getMethodByDesc(desc: String): Method = getMethodByDesc(desc, this)

/**
 * Hàm mở rộng để lấy phương thức thông qua Descriptor
 * @param desc Descriptor
 * @return Phương thức tìm thấy, trả về null nếu không tìm thấy
 */
fun ClassLoader.getMethodByDescOrNull(desc: String): Method? = getMethodByDescOrNull(desc, this)
