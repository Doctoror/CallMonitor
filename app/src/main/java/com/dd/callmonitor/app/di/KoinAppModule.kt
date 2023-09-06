package com.dd.callmonitor.app.di

import android.app.NotificationManager
import android.content.res.Resources
import com.dd.callmonitor.app.notifications.RegisterNotificationChannelUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinAppModule() = module {

    factory {
        RegisterNotificationChannelUseCase(
            notificationManager = androidContext().getSystemService(NotificationManager::class.java),
            resources = get()
        )
    }

    factory<Resources> { androidContext().resources }
}
