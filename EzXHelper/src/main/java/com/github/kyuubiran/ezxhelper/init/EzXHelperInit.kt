package com.github.kyuubiran.ezxhelper.init

import android.app.AndroidAppHelper
import android.content.Context
import android.content.res.Resources
import android.content.res.XModuleResources
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.init.InitFields.ezXClassLoader
import com.github.kyuubiran.ezxhelper.init.InitFields.hostPackageName
import com.github.kyuubiran.ezxhelper.init.InitFields.modulePath
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.github.kyuubiran.ezxhelper.utils.*
import com.github.kyuubiran.ezxhelper.utils.parasitics.ActivityHelper
import com.github.kyuubiran.ezxhelper.utils.parasitics.ActivityProxyManager
import com.github.kyuubiran.ezxhelper.utils.parasitics.TransferActivity
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

object EzXHelperInit {
    /**
     * Khởi tạo bắt buộc khi sử dụng thư viện này
     * Nên được gọi đầu tiên trong phương thức handleLoadPackage
     * @see IXposedHookLoadPackage.handleLoadPackage
     * @see XC_LoadPackage.LoadPackageParam
     */
    fun initHandleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        setEzClassLoader(lpparam.classLoader)
        setHostPackageName(lpparam.packageName)
    }

    /**
     * Khởi tạo Zygote để sử dụng đường dẫn module và tài nguyên module
     * @see IXposedHookZygoteInit.initZygote
     */
    fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
        moduleRes = XModuleResources.createInstance(modulePath, null)
    }

    /**
     * Thiết lập class loader được sử dụng bởi thư viện này
     *
     * Lưu ý: Thông thường, nên sử dụng class loader được cung cấp bởi framework
     *
     * Nhưng một số ứng dụng sẽ sửa đổi class loader của chúng, trong trường hợp này, vui lòng tự thiết lập class loader thời gian chạy
     * @param classLoader Class loader
     */
    fun setEzClassLoader(classLoader: ClassLoader) {
        ezXClassLoader = classLoader
    }

    /**
     * Thiết lập tên gói của ứng dụng chủ
     */
    fun setHostPackageName(packageName: String) {
        hostPackageName = packageName
    }

    /**
     * Khởi tạo ApplicationContext toàn cục
     * @param context ctx
     * @param addPath có thêm đường dẫn tài nguyên module vào ctx không
     * @param initModuleResources có khởi tạo moduleRes không
     */
    fun initAppContext(
        context: Context = AndroidAppHelper.currentApplication(),
        addPath: Boolean = false,
        initModuleResources: Boolean = false
    ) {
        appContext = context
        if (addPath) addModuleAssetPath(appContext)
        if (initModuleResources) moduleRes = context.resources
    }


    /**
     * Thiết lập Logger tùy chỉnh, xung đột với setLogXp setLogTag setToastTag!
     */
    fun setLogger(log: Logger) {
        Log.currentLogger = log
    }


    /**
     * Thiết lập có xuất log ra Xposed không
     */
    fun setLogXp(toXp: Boolean) {
        Log.currentLogger.logXp = toXp
    }

    /**
     * Thiết lập tag cho log
     */
    fun setLogTag(tag: String) {
        Log.currentLogger.logTag = tag
    }

    /**
     * Thiết lập Tag cho Log.toast
     * Nếu không thiết lập sẽ sử dụng TAG của log
     * @see Log.toast
     */
    fun setToastTag(tag: String) {
        Log.currentLogger.toastTag = tag
    }

    /**
     * Thêm đường dẫn tài nguyên của module vào Context.resources, cho phép truy cập trực tiếp tài nguyên thông qua R.xx.xxx
     *
     * Yêu cầu:
     *
     * 1. Sửa đổi ID tài nguyên trong build.gradle của dự án (miễn là không xung đột với ứng dụng chủ) như sau:
     *
     * Kotlin Gradle DSL:
     * androidResources.additionalParameters("--allow-reserved-package-id", "--package-id", "0x64")
     *
     * Groovy:
     * aaptOptions.additionalParameters '--allow-reserved-package-id', '--package-id', '0x64'
     *
     * 2. Đã thực thi EzXHelperInit.initZygote
     *
     * 3. Gọi trước khi sử dụng tài nguyên
     *
     * Ví dụ: Trong Activity:
     * init { addModuleAssetPath(this) }
     *
     * @see initZygote
     *
     */
    fun addModuleAssetPath(context: Context) {
        addModuleAssetPath(context.resources)
    }

    fun addModuleAssetPath(resources: Resources) {
        resources.assets.invokeMethod(
            "addAssetPath",
            args(modulePath),
            argTypes(String::class.java)
        )
    }

    /**
     * Khởi tạo các thành phần cần thiết để khởi chạy Activity chưa đăng ký
     * Cần được gọi trước initSubActivity và không nên gọi quá sớm
     *
     * @param modulePackageName Tên gói của module
     * @param hostActivityProxyName Tên Activity proxy của ứng dụng chủ
     * @param moduleClassLoader Class loader của module
     * @param hostClassLoader Class loader của ứng dụng chủ
     * @see initSubActivity
     * @see ActivityProxyManager
     * @see TransferActivity
     */
    fun initActivityProxyManager(
        modulePackageName: String,
        hostActivityProxyName: String,
        moduleClassLoader: ClassLoader,
        hostClassLoader: ClassLoader = AndroidAppHelper.currentApplication().classLoader!!
    ) {
        ActivityProxyManager.MODULE_PACKAGE_NAME_ID = modulePackageName
        ActivityProxyManager.ACTIVITY_PROXY_INTENT =
            "${modulePackageName.replace('.', '_')}_intent_proxy"
        ActivityProxyManager.HOST_ACTIVITY_PROXY_NAME = hostActivityProxyName
        ActivityProxyManager.MODULE_CLASS_LOADER = moduleClassLoader
        ActivityProxyManager.HOST_CLASS_LOADER = hostClassLoader
    }

    /**
     * Khởi tạo để khởi chạy Activity chưa đăng ký
     *
     * Cần gọi initActivityProxyManager trước
     * @see initActivityProxyManager
     *
     * Cần sử dụng addModuleAssetPath nên phải gọi initZygote
     * @see initZygote
     * @see addModuleAssetPath
     *
     * Cần được gọi sau Application.onCreate và chỉ cần gọi một lần
     * Và tất cả Activity của module cần kế thừa từ TransferActivity
     */
    fun initSubActivity() {
        ActivityHelper.initSubActivity()
    }
}
