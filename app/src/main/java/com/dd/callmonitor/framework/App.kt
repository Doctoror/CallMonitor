package com.dd.callmonitor.framework

import android.app.Application
import com.dd.callmonitor.domain.notifications.NOTIFICATION_CHANNEL_ID_SERVER_STATUS
import com.dd.callmonitor.domain.notifications.RegisterNotificationChannelUseCase
import com.dd.callmonitor.framework.lifecycle.ActivityMonitor
import org.koin.android.ext.android.inject

class App : Application() {

    private val activityMonitor: ActivityMonitor by inject()

    private val registerNotificationChannelUseCase: RegisterNotificationChannelUseCase by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin(this)
        registerActivityLifecycleCallbacks(activityMonitor)
        registerNotificationChannelUseCase(NOTIFICATION_CHANNEL_ID_SERVER_STATUS)
    }
}
