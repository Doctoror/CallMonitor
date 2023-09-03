package com.dd.callmonitor.domain.server

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.dd.callmonitor.R
import com.dd.callmonitor.domain.notifications.NOTIFICATION_CHANNEL_ID_SERVER_STATUS
import com.dd.callmonitor.presentation.main.MainActivity

class MakeServerStatusNotificationUseCase {

    operator fun invoke(context: Context, contentTitle: CharSequence) = NotificationCompat
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
