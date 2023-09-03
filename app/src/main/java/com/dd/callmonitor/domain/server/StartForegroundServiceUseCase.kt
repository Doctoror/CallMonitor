package com.dd.callmonitor.domain.server

import android.app.Notification
import android.app.Service
import android.content.pm.ServiceInfo
import android.os.Build

class StartForegroundServiceUseCase {

    operator fun invoke(
        service: Service,
        notificationId: Int,
        notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            service.startForeground(
                notificationId,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            service.startForeground(
                notificationId,
                notification
            )
        }
    }
}
