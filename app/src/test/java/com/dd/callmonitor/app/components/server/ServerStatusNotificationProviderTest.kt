package com.dd.callmonitor.app.components.server

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.dd.callmonitor.R
import com.dd.callmonitor.app.components.NoKoinTestApp
import com.dd.callmonitor.app.components.main.MainActivity
import com.dd.callmonitor.app.notifications.NOTIFICATION_CHANNEL_ID_SERVER_STATUS
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = NoKoinTestApp::class)
class ServerStatusNotificationProviderTest {

    private val context = RuntimeEnvironment.getApplication()
    private val underTest = ServerStatusNotificationProvider()

    @Test
    fun provides() {
        val contentTitle = "Some title"

        val output = underTest.provide(context, contentTitle)

        assertEquals(
            NotificationCompat
                .Builder(
                    context,
                    NOTIFICATION_CHANNEL_ID_SERVER_STATUS
                )
                .setAutoCancel(true)
                .setContentIntent(mainActivityContentIntent())
                .setContentTitle(contentTitle)
                .setSmallIcon(R.drawable.baseline_power_settings_new_24)
                .build()
                .toString(),
            output.toString()
        )
    }

    @Test
    fun contentIntentLeadsToMainActivity() {
        val output = underTest.provide(context, "Some title")

        assertEquals(
            mainActivityContentIntent(),
            output.contentIntent
        )
    }

    private fun mainActivityContentIntent() = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )
}
