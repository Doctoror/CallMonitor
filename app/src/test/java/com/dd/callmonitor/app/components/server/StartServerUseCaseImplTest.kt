package com.dd.callmonitor.app.components.server

import android.content.Intent
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class StartServerUseCaseImplTest {

    private val application = RuntimeEnvironment.getApplication()

    private val applicationShadow = Shadows.shadowOf(application)

    private val underTest = StartServerUseCaseImpl(application)

    @Test
    fun startsServerService() {
        underTest()

        assertTrue(
            Intent(application, ServerService::class.java)
                .apply { action = SERVER_SERVICE_ACTION_START }
                .filterEquals(applicationShadow.nextStartedService),
        )
    }
}
