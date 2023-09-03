package com.dd.callmonitor.framework

import android.content.Context
import com.dd.callmonitor.data.notifications.koinNotificationsDataModule
import com.dd.callmonitor.data.server.koinServerModule
import com.dd.callmonitor.domain.koinDomainModule
import com.dd.callmonitor.domain.notifications.koinNotificationsDomainModule
import com.dd.callmonitor.presentation.main.koinMainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun startKoin(context: Context) = startKoin {
    androidContext(context)
    modules(
        koinNotificationsDataModule(),
        koinNotificationsDomainModule(),
        koinDomainModule(),
        koinFrameworkModule(),
        koinMainModule(),
        koinServerModule()
    )
}
