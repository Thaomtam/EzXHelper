package com.github.kyuubiran.ezxhelper.utils.parasitics

import android.app.Activity
import android.os.Bundle

/**
 * Tất cả các Activity cần khởi chạy trong ứng dụng chủ đều phải kế thừa từ TransferActivity
 *
 * Hoặc bạn có thể kế thừa từ Activity của riêng mình và ghi đè hai hàm này theo cách dưới đây
 */
open class TransferActivity : Activity() {
    override fun getClassLoader(): ClassLoader {
        return FixedClassLoader(
            ActivityProxyManager.MODULE_CLASS_LOADER,
            ActivityProxyManager.HOST_CLASS_LOADER
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val windowState = savedInstanceState.getBundle("android:viewHierarchyState")
        windowState?.let {
            it.classLoader = TransferActivity::class.java.classLoader!!
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}