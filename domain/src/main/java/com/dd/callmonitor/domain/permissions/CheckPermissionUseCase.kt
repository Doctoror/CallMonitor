package com.dd.callmonitor.domain.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class CheckPermissionUseCase(private val context: Context) {

    operator fun <R> invoke(
        permission: ApiLevelPermission,
        whenDenied: () -> R,
        whenGranted: () -> R,
    ): R {
        if (Build.VERSION.SDK_INT < permission.addedInSdkInt) {
            return whenGranted()
        }

        if (context.checkSelfPermission(permission.permission) == PackageManager.PERMISSION_GRANTED) {
            return whenGranted()
        }

        return whenDenied()
    }
}
