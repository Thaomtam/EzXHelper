package com.github.kyuubiran.ezxhelper.utils.parasitics

object ActivityProxyManager {
    /**
     * Dùng để phân biệt intent của ứng dụng chủ và module
     */
    lateinit var ACTIVITY_PROXY_INTENT: String

    /**
     * Tên gói của module, có thể sử dụng BuildConfig.APPLICATION_ID để lấy
     */
    lateinit var MODULE_PACKAGE_NAME_ID: String

    /**
     * Tên Activity proxy của ứng dụng chủ
     */
    lateinit var HOST_ACTIVITY_PROXY_NAME: String

    /**
     * Class loader của module, có thể sử dụng TênLớpModule.javaclass.classLoader để lấy
     */
    lateinit var MODULE_CLASS_LOADER: ClassLoader

    /**
     * Class loader của ứng dụng chủ, có thể sử dụng AndroidAppHelper.currentApplication().classLoader để lấy
     */
    lateinit var HOST_CLASS_LOADER: ClassLoader
}