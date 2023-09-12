package com.dd.callmonitor.app.components.server

import android.content.Intent
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class StopServerUseCaseImplTest {

    private val application = RuntimeEnvironment.getApplication()

    private val applicationShadow = Shadows.shadowOf(application)

    private val underTest = StopServerUseCaseImpl(application)

    @Test
    fun stopsServerService() {
        underTest()

        assertTrue(
            Intent(application, ServerService::class.java)
                .apply { action = SERVER_SERVICE_ACTION_STOP }
                .filterEquals(applicationShadow.nextStartedService),
        )
    }
}
