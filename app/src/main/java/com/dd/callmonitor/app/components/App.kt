package com.dd.callmonitor.app.components

import android.app.Application
import com.dd.callmonitor.app.di.startKoin
import com.dd.callmonitor.app.notifications.NOTIFICATION_CHANNEL_ID_SERVER_STATUS
import com.dd.callmonitor.app.notifications.NotificationChannelRegistrator
import org.koin.android.ext.android.inject

class App : Application() {

    private val notificationChannelRegistrator: NotificationChannelRegistrator by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin(this)
        notificationChannelRegistrator(NOTIFICATION_CHANNEL_ID_SERVER_STATUS)
    }
}
