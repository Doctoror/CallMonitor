package com.dd.callmonitor.app.components.server

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.dd.callmonitor.R
import com.dd.callmonitor.app.components.main.MainActivity
import com.dd.callmonitor.app.notifications.NOTIFICATION_CHANNEL_ID_SERVER_STATUS

class MakeServerStatusNotificationUseCase {

    operator fun invoke(context: Context, contentTitle: CharSequence): Notification =
        NotificationCompat
            .Builder(
                context,
                NOTIFICATION_CHANNEL_ID_SERVER_STATUS
            )
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setContentTitle(contentTitle)
            .setSmallIcon(R.drawable.baseline_power_settings_new_24)
            .build()
}
