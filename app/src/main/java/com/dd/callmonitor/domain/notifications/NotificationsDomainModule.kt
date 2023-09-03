package com.dd.callmonitor.domain.notifications

import android.app.NotificationManager
import com.dd.callmonitor.domain.server.MakeForegroundServerStatusNotificationUseCase
import com.dd.callmonitor.domain.server.ShowServerFailedNotificationUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinNotificationsDomainModule() = module {

    factory {
        CheckPostNotificationsPermissionUseCase()
    }

    factory {
        MakeForegroundServerStatusNotificationUseCase(get())
    }

    factory {
        RegisterNotificationChannelUseCase(
            notificationManager = androidContext().getSystemService(NotificationManager::class.java),
            resources = get()
        )
    }

    factory {
        ShowServerFailedNotificationUseCase(get())
    }
}
