package com.dd.callmonitor.app.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.dd.callmonitor.R
import com.dd.callmonitor.app.notifications.NOTIFICATION_CHANNEL_ID_SERVER_STATUS
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class AppTest {

    private val notificationManagerShadow = Shadows.shadowOf(
        RuntimeEnvironment
            .getApplication()
            .getSystemService(NotificationManager::class.java)
    )

    private val resources = RuntimeEnvironment.getApplication().resources

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun registersNotificationChannelPostOreo() {
        assertEquals(
            listOf(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_SERVER_STATUS,
                    resources.getString(R.string.notification_channel_name_server_status),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            ),
            notificationManagerShadow.notificationChannels
        )
    }
}
