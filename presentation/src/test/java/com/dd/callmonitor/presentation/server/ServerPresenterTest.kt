package com.dd.callmonitor.presentation.server

import com.dd.callmonitor.domain.connectivity.ConnectivityState
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.server.Server
import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.domain.server.ServerStateProvider
import com.dd.callmonitor.domain.util.Optional
import com.dd.callmonitor.presentation.R
import com.dd.callmonitor.presentation.testutil.executeBlockAndCollectFromFlow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class ServerPresenterTest {

    private val observeWifiConnectivityUseCase: ObserveWifiConnectivityUseCase = mockk()
    private val foregroundServiceStatusMessageProvider: ForegroundServiceStatusMessageProvider =
        mockk()
    private val resources = RuntimeEnvironment.getApplication().resources
    private val server: Server = mockk()
    private val serverStateProvider = ServerStateProvider()
    private val viewModel = ServerViewModel()

    private val underTest = ServerPresenter(
        observeWifiConnectivityUseCase,
        foregroundServiceStatusMessageProvider,
        resources,
        CoroutineScope(Dispatchers.Unconfined),
        server,
        serverStateProvider,
        viewModel
    )

    @Before
    fun before() {
        every { foregroundServiceStatusMessageProvider(any()) } returns Optional.empty()
        every { observeWifiConnectivityUseCase() } returns flowOf()
        coEvery { server.stopIfRunning() } returns Unit
    }

    @Test
    fun emitsGenericErrorNotificationOnServerErrorGeneric() {
        val collectedNotifications = executeBlockAndCollectFromFlow(viewModel.normalNotification) {
            underTest.onCreate()
            serverStateProvider.state.value = ServerState.Error(ServerError.GENERIC)
        }

        assertEquals(
            listOf(resources.getText(R.string.server_power_button_when_error_label_generic)),
            collectedNotifications
        )
    }

    @Test
    fun emitsForegroundNotificationOnServerState() {
        val serverState = ServerState.Initialising
        val notificationMessage = "Notification message"
        every {
            foregroundServiceStatusMessageProvider.invoke(serverState)
        } returns Optional.of(notificationMessage)

        val collectedNotifications = executeBlockAndCollectFromFlow(
            viewModel.foregroundNotification
        ) {
            underTest.onCreate()
            serverStateProvider.state.value = serverState
        }

        assertEquals(listOf(notificationMessage), collectedNotifications)
    }

    @Test
    fun doesNotEmitForegroundNotificationWhenNoMessageProvided() {
        val serverState = ServerState.Initialising
        every {
            foregroundServiceStatusMessageProvider.invoke(serverState)
        } returns Optional.empty()

        val collectedNotifications = executeBlockAndCollectFromFlow(
            viewModel.foregroundNotification
        ) {
            underTest.onCreate()
            serverStateProvider.state.value = serverState
        }

        assertEquals(emptyList<CharSequence>(), collectedNotifications)
    }

    @Test
    fun emitsFinishSignalOnError() {
        val collectedFinishEvents = executeBlockAndCollectFromFlow(underTest.finishEvents()) {
            underTest.onCreate()
            serverStateProvider.state.value = ServerState.Error(ServerError.GENERIC)
        }

        assertEquals(listOf(Unit), collectedFinishEvents)
    }

    @Test
    fun stopsServerAndEmitsFinishSignalOnDisconnectedConnectivity() {
        every { observeWifiConnectivityUseCase() } returns flowOf(ConnectivityState.Disconnected)

        val collectedFinishEvents = executeBlockAndCollectFromFlow(underTest.finishEvents()) {
            underTest.onCreate()
        }

        coVerify { server.stopIfRunning() }
        assertEquals(listOf(Unit), collectedFinishEvents)
    }

    @Test
    fun stopIfRunningBlockingForwardsStopToServer() {
        underTest.stopIfRunningBlocking()
        coVerify { server.stopIfRunning() }
    }

    @Test
    fun stopIfRunningAndExitForwardsStopToServerAndEmitsFinishSignal() {
        val collected = executeBlockAndCollectFromFlow(underTest.finishEvents()) {
            underTest.stopIfRunningAndExit()
        }

        coVerify { server.stopIfRunning() }
        assertEquals(listOf(Unit), collected)
    }
}
