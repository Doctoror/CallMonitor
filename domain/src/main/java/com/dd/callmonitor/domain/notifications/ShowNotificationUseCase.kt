package com.dd.callmonitor.domain.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase

class ShowNotificationUseCase(
    private val checkPermissionUseCase: CheckPermissionUseCase,
) {

    @SuppressLint("MissingPermission")
    operator fun invoke(context: Context, id: Int, notification: Notification) {
        checkPermissionUseCase(
            permission = ApiLevelPermissions.POST_NOTIFICATIONS,
            whenDenied = {},
            whenGranted = {
                NotificationManagerCompat
                    .from(context)
                    .notify(id, notification)
            }
        )
    }
}
