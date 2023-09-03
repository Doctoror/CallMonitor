package com.dd.callmonitor.domain.notifications

import android.Manifest
import android.os.Build
import androidx.activity.result.ActivityResultLauncher

class RequestNotificationsPermissionUseCase(
    private val activityResultLauncher: ActivityResultLauncher<String>
) {

    operator fun invoke() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            throw IllegalStateException(
                "This is not expected to be called on SDKs lower than ${Build.VERSION_CODES.TIRAMISU}"
            )
        }
    }
}
