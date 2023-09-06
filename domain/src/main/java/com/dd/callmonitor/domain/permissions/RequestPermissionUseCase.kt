package com.dd.callmonitor.domain.permissions

import android.os.Build
import androidx.activity.result.ActivityResultLauncher

open class RequestPermissionUseCase(
    private val launcher: ActivityResultLauncher<String>,
    private val permission: ApiLevelPermission
) {

    operator fun invoke() {
        if (Build.VERSION.SDK_INT >= permission.addedInSdkInt) {
            launcher.launch(permission.permission)
        } else {
            throw IllegalStateException(
                "This is not expected to be called on SDKs lower than ${permission.addedInSdkInt}"
            )
        }
    }
}
