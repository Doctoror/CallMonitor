package com.dd.callmonitor.domain.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationManagerCompat

class ShowNotificationUseCase(
    private val checkPostNotificationsPermissionUseCase: CheckPostNotificationsPermissionUseCase,
) {

    @SuppressLint("MissingPermission")
    operator fun invoke(context: Context, id: Int, notification: Notification) {
        if (checkPostNotificationsPermissionUseCase(context) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat
                .from(context)
                .notify(id, notification)
        }
    }
}
