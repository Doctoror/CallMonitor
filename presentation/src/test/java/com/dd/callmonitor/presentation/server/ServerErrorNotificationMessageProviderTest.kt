package com.dd.callmonitor.presentation.server

import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.presentation.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class ServerErrorNotificationMessageProviderTest {

    private val resources = RuntimeEnvironment.getApplication().resources

    private val underTest = ServerErrorNotificationMessageProvider(resources)

    @Test
    fun returnsGenericErrorMessage() {
        assertEquals(
            resources.getText(R.string.server_power_button_when_error_label_generic),
            underTest.provide(ServerError.GENERIC)
        )
    }

    @Test
    fun returnsNoConnectivityErrorMessage() {
        assertEquals(
            resources.getText(R.string.server_power_button_when_error_label_not_connected),
            underTest.provide(ServerError.NO_CONNECTIVITY)
        )
    }

    @Test
    fun returnsNoHostAddressErrorMessage() {
        assertEquals(
            resources.getText(R.string.server_power_button_when_error_label_no_host_address),
            underTest.provide(ServerError.NO_HOST_ADDRESS)
        )
    }
}
