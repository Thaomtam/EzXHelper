package com.github.kyuubiran.ezxhelper.utils

import java.lang.reflect.Constructor
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Static không
 */
val Member.isStatic: Boolean
    inline get() = Modifier.isStatic(this.modifiers)
val Member.isNotStatic: Boolean
    inline get() = !this.isStatic

val Class<*>.isStatic: Boolean
    inline get() = Modifier.isStatic(this.modifiers)
val Class<*>.isNotStatic: Boolean
    inline get() = !this.isStatic

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Public không
 */
val Member.isPublic: Boolean
    inline get() = Modifier.isPublic(this.modifiers)
val Member.isNotPublic: Boolean
    inline get() = !this.isPublic

val Class<*>.isPublic: Boolean
    inline get() = Modifier.isPublic(this.modifiers)
val Class<*>.isNotPublic: Boolean
    inline get() = !this.isPublic

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Protected không
 */
val Member.isProtected: Boolean
    inline get() = Modifier.isProtected(this.modifiers)
val Member.isNotProtected: Boolean
    inline get() = !this.isProtected

val Class<*>.isProtected: Boolean
    inline get() = Modifier.isProtected(this.modifiers)
val Class<*>.isNotProtected: Boolean
    inline get() = !this.isProtected

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Private không
 */
val Member.isPrivate: Boolean
    inline get() = Modifier.isPrivate(this.modifiers)
val Member.isNotPrivate: Boolean
    inline get() = !this.isPrivate

val Class<*>.isPrivate: Boolean
    inline get() = Modifier.isPrivate(this.modifiers)
val Class<*>.isNotPrivate: Boolean
    inline get() = !this.isPrivate

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Final không
 */
val Member.isFinal: Boolean
    inline get() = Modifier.isFinal(this.modifiers)
val Member.isNotFinal: Boolean
    inline get() = !this.isFinal

val Class<*>.isFinal: Boolean
    inline get() = Modifier.isFinal(this.modifiers)
val Class<*>.isNotFinal: Boolean
    inline get() = !this.isFinal

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Native không
 */
val Member.isNative: Boolean
    inline get() = Modifier.isNative(this.modifiers)
val Member.isNotNative: Boolean
    inline get() = !this.isNative

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Synchronized không
 */
val Member.isSynchronized: Boolean
    inline get() = Modifier.isSynchronized(this.modifiers)
val Member.isNotSynchronized: Boolean
    inline get() = !this.isSynchronized

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Abstract không
 */
val Member.isAbstract: Boolean
    inline get() = Modifier.isAbstract(this.modifiers)
val Member.isNotAbstract: Boolean
    inline get() = !this.isAbstract

val Class<*>.isAbstract: Boolean
    inline get() = Modifier.isAbstract(this.modifiers)
val Class<*>.isNotAbstract: Boolean
    inline get() = !this.isAbstract

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Transient không
 */
val Member.isTransient: Boolean
    inline get() = Modifier.isTransient(this.modifiers)
val Member.isNotTransient: Boolean
    inline get() = !this.isTransient

/**
 * Thuộc tính mở rộng để kiểm tra có phải là Volatile không
 */
val Member.isVolatile: Boolean
    inline get() = Modifier.isVolatile(this.modifiers)
val Member.isNotVolatile: Boolean
    inline get() = !this.isVolatile

/**
 * Thuộc tính mở rộng để lấy số lượng tham số của phương thức
 */
val Method.paramCount: Int
    inline get() = this.parameterTypes.size

/**
 * Thuộc tính mở rộng để lấy số lượng tham số của constructor
 */
val Constructor<*>.paramCount: Int
    inline get() = this.parameterTypes.size

/**
 * Thuộc tính mở rộng để kiểm tra phương thức có tham số không
 */
val Method.emptyParam: Boolean
    inline get() = this.paramCount == 0
val Method.notEmptyParam: Boolean
    inline get() = this.paramCount != 0

/**
 * Thuộc tính mở rộng để kiểm tra constructor có tham số không
 */
val Constructor<*>.emptyParam: Boolean
    inline get() = this.paramCount == 0
val Constructor<*>.notEmptyParam: Boolean
    inline get() = this.paramCount != 0
