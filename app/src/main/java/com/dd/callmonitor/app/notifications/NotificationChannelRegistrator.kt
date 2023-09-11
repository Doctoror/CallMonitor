package com.dd.callmonitor.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.res.Resources
import android.os.Build
import com.dd.callmonitor.R

class NotificationChannelRegistrator(
    private val notificationManager: NotificationManager,
    private val resources: Resources
) {

    operator fun invoke(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    resources.getString(R.string.notification_channel_name_server_status),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }
}
