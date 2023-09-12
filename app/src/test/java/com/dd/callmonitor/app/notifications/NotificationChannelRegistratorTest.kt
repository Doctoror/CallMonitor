package com.dd.callmonitor.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.dd.callmonitor.R
import com.dd.callmonitor.app.components.NoKoinTestApp
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = NoKoinTestApp::class)
class NotificationChannelRegistratorTest {

    private val channelId = "channelId"

    private val notificationManager = RuntimeEnvironment
        .getApplication()
        .getSystemService(NotificationManager::class.java)

    private val notificationManagerShadow = Shadows.shadowOf(notificationManager)

    private val resources = RuntimeEnvironment.getApplication().resources

    private val underTest = NotificationChannelRegistrator(notificationManager, resources)

    @Test
    @Config(sdk = [Build.VERSION_CODES.N_MR1])
    fun doesNotThrowNoSuchMethodErrorPreOreo() {
        underTest(channelId)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O])
    fun registersPostOreo() {
        underTest(channelId)

        assertEquals(
            listOf(
                NotificationChannel(
                    channelId,
                    resources.getString(R.string.notification_channel_name_server_status),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            ),
            notificationManagerShadow.notificationChannels
        )
    }
}
