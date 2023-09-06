package com.dd.callmonitor.app.components

import android.app.Application
import com.dd.callmonitor.app.di.startKoin
import com.dd.callmonitor.app.notifications.NOTIFICATION_CHANNEL_ID_SERVER_STATUS
import com.dd.callmonitor.app.notifications.RegisterNotificationChannelUseCase
import org.koin.android.ext.android.inject

class App : Application() {

    private val registerNotificationChannelUseCase: RegisterNotificationChannelUseCase by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin(this)
        registerNotificationChannelUseCase(NOTIFICATION_CHANNEL_ID_SERVER_STATUS)
    }
}
