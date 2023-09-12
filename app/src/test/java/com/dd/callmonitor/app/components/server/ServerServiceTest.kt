package com.dd.callmonitor.app.components.server

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import com.dd.callmonitor.app.components.NoKoinTestApp
import com.dd.callmonitor.app.notifications.NOTIFICATION_ID_SERVER_STATUS_OTHER
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.presentation.server.ServerPresenter
import com.dd.callmonitor.presentation.server.ServerViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.android.controller.ServiceController
import org.robolectric.annotation.Config
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
@Config(application = NoKoinTestApp::class)
class ServerServiceTest {

    private val application = RuntimeEnvironment.getApplication()

    private val checkPermissionUseCase = CheckPermissionUseCase(application)

    private val serverStatusNotificationProvider: ServerStatusNotificationProvider = mockk()

    private val presenter: ServerPresenter = mockk()

    private val viewModel = ServerViewModel()

    private val underTest = ServerService()

    private val underTestShadow = Shadows.shadowOf(underTest)

    private val serviceController = ServiceController.of(
        underTest,
        Intent(application, ServerService::class.java)
    )

    @Before
    fun before() {
        every { presenter.onCreate() } returns Unit
        every { presenter.finishEvents() } returns flowOf()
        every { presenter.startServer() } returns Unit
        every { presenter.stopIfRunningAndExit() } returns Unit
        every { presenter.stopIfRunningBlocking() } returns Unit
        every { presenter.viewModel } returns viewModel

        startKoin {
            androidContext(application)
            modules(koinServerModule())
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun showsForegroundNotificationFromViewModel() = runTest {
        serviceController.create()
        val notificationTitle = "Server initializing"
        val foregroundNotification: Notification = mockk()
        every { serverStatusNotificationProvider.provide(underTest, notificationTitle) } returns
                foregroundNotification

        viewModel.foregroundNotification.emit(notificationTitle)

        assertEquals(
            foregroundNotification,
            underTestShadow.lastForegroundNotification
        )
    }

    @Test
    fun showsNormalNotificationFromViewModelWhenPermissionGranted() = runTest {
        serviceController.create()
        underTestShadow.grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        val notificationTitle = "Server error"
        val notification: Notification = mockk()
        val notificationManagerShadow = Shadows.shadowOf(
            application.getSystemService(NotificationManager::class.java)
        )

        every { serverStatusNotificationProvider.provide(underTest, notificationTitle) } returns
                notification

        viewModel.normalNotification.emit(notificationTitle)

        assertEquals(
            notification,
            notificationManagerShadow.getNotification(NOTIFICATION_ID_SERVER_STATUS_OTHER),
        )
    }

    @Test
    fun doesNotShowNormalNotificationFromViewModelWhenPermissionNotGranted() = runTest {
        serviceController.create()
        val notificationTitle = "Server error"
        val notification: Notification = mockk()
        val notificationManagerShadow = Shadows.shadowOf(
            application.getSystemService(NotificationManager::class.java)
        )

        every { serverStatusNotificationProvider.provide(underTest, notificationTitle) } returns
                notification

        viewModel.normalNotification.emit(notificationTitle)

        assertNull(notificationManagerShadow.getNotification(NOTIFICATION_ID_SERVER_STATUS_OTHER))
    }

    @Test
    fun startsServerOnStartAction() {
        val startActionServiceController = ServiceController.of(
            ServerService(),
            Intent(application, ServerService::class.java)
                .apply { action = SERVER_SERVICE_ACTION_START }
        )

        startActionServiceController.create()
        startActionServiceController.startCommand(0, 0)

        verify { presenter.startServer() }
    }

    @Test
    fun stopsServerBlockingOnDestroy() {
        serviceController.create()

        serviceController.destroy()

        verify { presenter.stopIfRunningBlocking() }
    }

    @Test
    fun stopsServerOnStopAction() {
        val stopServiceController = ServiceController.of(
            ServerService(),
            Intent(application, ServerService::class.java)
                .apply { action = SERVER_SERVICE_ACTION_STOP }
        )

        stopServiceController.create()
        stopServiceController.startCommand(0, 0)

        verify { presenter.stopIfRunningAndExit() }
    }

    @Test
    fun stopsSelfOnFinishEvent() {
        every { presenter.finishEvents() } returns flowOf(Unit)

        serviceController.create()

        assertTrue(underTestShadow.isStoppedBySelf)
    }

    @Test(expected = IllegalArgumentException::class)
    fun throwsIllegalArgumentExceptionOnUnexpectedAction() {
        val unexpectedActionServiceController = ServiceController.of(
            ServerService(),
            Intent(application, ServerService::class.java)
                .apply { action = "SomeAction" }
        )

        unexpectedActionServiceController.create()
        unexpectedActionServiceController.startCommand(0, 0)
    }

    private fun koinServerModule() = module {
        factory { checkPermissionUseCase }

        factory { presenter }

        factory { serverStatusNotificationProvider }
    }
}
