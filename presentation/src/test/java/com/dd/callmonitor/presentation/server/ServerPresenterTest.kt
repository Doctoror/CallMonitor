package com.dd.callmonitor.presentation.server

import app.cash.turbine.test
import com.dd.callmonitor.domain.connectivity.ConnectivityState
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.server.Server
import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.domain.server.ServerStateProvider
import com.dd.callmonitor.domain.util.Optional
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ServerPresenterTest {

    private val observeWifiConnectivityUseCase: ObserveWifiConnectivityUseCase = mockk()
    private val foregroundServiceStatusMessageProvider: ForegroundServiceStatusMessageProvider =
        mockk()
    private val scope = TestScope()
    private val server: Server = mockk()
    private val serverErrorNotificationMessageProvider: ServerErrorNotificationMessageProvider =
        mockk()
    private val serverStateProvider = ServerStateProvider()
    private val viewModel = ServerViewModel()

    private val underTest = ServerPresenter(
        observeWifiConnectivityUseCase,
        foregroundServiceStatusMessageProvider,
        scope,
        server,
        serverErrorNotificationMessageProvider,
        serverStateProvider,
        viewModel
    )

    @Before
    fun before() {
        every { foregroundServiceStatusMessageProvider(any()) } returns Optional.empty()
        every { observeWifiConnectivityUseCase() } returns flowOf()
        every { serverErrorNotificationMessageProvider.provide(any()) } returns ""
        coJustRun { server.stopIfRunning() }
    }

    @Test
    fun emitsServerErrorNotificationFromMessageProvider() = runTest {
        val error = ServerError.GENERIC
        val errorMessage = "Error message"
        every { serverErrorNotificationMessageProvider.provide(error) } returns errorMessage

        viewModel.normalNotification.test {

            underTest.onCreate()
            scope.advanceUntilIdle()

            serverStateProvider.state.value = ServerState.Error(error)
            scope.advanceUntilIdle()

            assertEquals(errorMessage, awaitItem())
        }
    }

    @Test
    fun emitsForegroundNotificationOnServerState() = runTest {
        val serverState = ServerState.Initialising
        val notificationMessage = "Notification message"
        every {
            foregroundServiceStatusMessageProvider.invoke(serverState)
        } returns Optional.of(notificationMessage)

        viewModel.foregroundNotification.test {

            underTest.onCreate()
            serverStateProvider.state.value = serverState

            scope.advanceUntilIdle()
            assertEquals(notificationMessage, awaitItem())
        }
    }

    @Test
    fun doesNotEmitForegroundNotificationWhenNoMessageProvided() = runTest {
        val serverState = ServerState.Initialising
        every {
            foregroundServiceStatusMessageProvider.invoke(serverState)
        } returns Optional.empty()


        viewModel.foregroundNotification.test {
            underTest.onCreate()
            serverStateProvider.state.value = serverState
            scope.advanceUntilIdle()

            expectNoEvents()
        }
    }

    @Test
    fun emitsFinishSignalOnError() = runTest {
        underTest.finishEvents().test {

            underTest.onCreate()
            scope.advanceUntilIdle()

            serverStateProvider.state.value = ServerState.Error(ServerError.GENERIC)
            scope.advanceUntilIdle()

            assertEquals(Unit, awaitItem())
        }
    }

    @Test
    fun stopsServerAndEmitsFinishSignalOnDisconnectedConnectivity() = runTest {
        every { observeWifiConnectivityUseCase() } returns flowOf(ConnectivityState.Disconnected)

        underTest.finishEvents().test {
            underTest.onCreate()
            scope.advanceUntilIdle()
            assertEquals(Unit, awaitItem())
        }

        coVerify { server.stopIfRunning() }
    }

    @Test
    fun stopIfRunningBlockingForwardsStopToServer() {
        underTest.stopIfRunningBlocking()
        coVerify { server.stopIfRunning() }
    }

    @Test
    fun stopIfRunningAndExitForwardsStopToServerAndEmitsFinishSignal() = runTest {
        underTest.finishEvents().test {
            underTest.stopIfRunningAndExit()
            scope.advanceUntilIdle()
            assertEquals(Unit, awaitItem())
        }
    }
}
