package com.github.kyuubiran.ezxhelper.init

import android.content.Context
import android.content.res.Resources

object InitFields {
    /**
     * AppContext toàn cục của ứng dụng chủ
     */
    lateinit var appContext: Context
        internal set

    /**
     * Kiểm tra AppContext toàn cục của ứng dụng chủ đã được khởi tạo chưa
     */
    val isAppContextInited: Boolean
        get() = this::appContext.isInitialized

    /**
     * ClassLoader được sử dụng khi gọi các hàm tải lớp của thư viện này
     */
    lateinit var ezXClassLoader: ClassLoader
        internal set

    /**
     * Kiểm tra ClassLoader đã được khởi tạo chưa
     */
    val isEzXClassLoaderInited: Boolean
        get() = this::ezXClassLoader.isInitialized

    /**
     * Đường dẫn module
     */
    lateinit var modulePath: String
        internal set

    /**
     * Kiểm tra đường dẫn module đã được khởi tạo chưa
     */
    val isModulePathInited: Boolean
        get() = this::modulePath.isInitialized

    /**
     * Tài nguyên module
     */
    lateinit var moduleRes: Resources
        internal set

    /**
     * Kiểm tra tài nguyên module đã được khởi tạo chưa
     */
    val isModuleResInited: Boolean
        get() = this::moduleRes.isInitialized

    /**
     * Tên gói của ứng dụng chủ
     */
    lateinit var hostPackageName: String
        internal set

    /**
     * Kiểm tra tên gói của ứng dụng chủ đã được khởi tạo chưa
     */
    val isHostPackageNameInited: Boolean
        get() = this::hostPackageName.isInitialized
}