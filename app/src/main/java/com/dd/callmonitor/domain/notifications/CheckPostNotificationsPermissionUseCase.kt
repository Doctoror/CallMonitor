package com.dd.callmonitor.domain.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class CheckPostNotificationsPermissionUseCase {

    operator fun invoke(context: Context): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            PackageManager.PERMISSION_GRANTED
        }
}
