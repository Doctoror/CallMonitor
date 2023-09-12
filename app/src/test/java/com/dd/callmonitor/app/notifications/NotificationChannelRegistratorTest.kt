package com.dd.callmonitor.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.dd.callmonitor.R
import com.dd.callmonitor.app.components.NoKoinTestApp
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = NoKoinTestApp::class)
class NotificationChannelRegistratorTest {

    private val channelId = "channelId"
    private val notificationManager: NotificationManager = mockk()
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
        every { notificationManager.createNotificationChannel(any()) } returns Unit

        underTest(channelId)

        verify {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    resources.getString(R.string.notification_channel_name_server_status),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }
}
