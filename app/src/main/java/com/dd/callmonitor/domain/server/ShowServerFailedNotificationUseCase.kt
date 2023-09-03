package com.dd.callmonitor.domain.server

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationManagerCompat
import com.dd.callmonitor.R
import com.dd.callmonitor.domain.notifications.CheckPostNotificationsPermissionUseCase
import com.dd.callmonitor.domain.notifications.NOTIFICATION_ID_SERVER_STATUS

class ShowServerFailedNotificationUseCase(
    private val checkPostNotificationsPermissionUseCase: CheckPostNotificationsPermissionUseCase,
    private val makeServerStatusNotificationUseCase: MakeServerStatusNotificationUseCase
) {

    @SuppressLint("MissingPermission")
    operator fun invoke(context: Context) {
        if (checkPostNotificationsPermissionUseCase(context) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat
                .from(context)
                .notify(
                    NOTIFICATION_ID_SERVER_STATUS,
                    makeServerStatusNotificationUseCase(
                        context,
                        context.getText(R.string.server_power_button_when_error_label)
                    )
                )
        }
    }
}
