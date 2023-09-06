package com.dd.callmonitor.app.components.server

import android.content.Intent
import android.os.IBinder
import com.dd.callmonitor.app.components.CoroutineScopeService
import com.dd.callmonitor.app.notifications.NOTIFICATION_ID_SERVER_STATUS
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.notifications.ShowNotificationUseCase
import com.dd.callmonitor.domain.notifications.StartForegroundServiceUseCase
import com.dd.callmonitor.presentation.server.ServerPresenter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

const val SERVER_SERVICE_ACTION_START = "ACTION_START"
const val SERVER_SERVICE_ACTION_STOP = "ACTION_STOP"

class ServerService : CoroutineScopeService() {

    private val makeServerStatusNotificationUseCase: MakeServerStatusNotificationUseCase by inject()

    private val showNotificationUseCase: ShowNotificationUseCase by inject()

    private val startForegroundServiceUseCase: StartForegroundServiceUseCase by inject()

    private val presenter: ServerPresenter by inject {
        parametersOf(scope)
    }

    private val viewModel by lazy { presenter.viewModel }

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            viewModel
                .foregroundNotification
                .map { makeServerStatusNotificationUseCase(this@ServerService, it) }
                .collect {
                    startForegroundServiceUseCase(
                        this@ServerService,
                        NOTIFICATION_ID_SERVER_STATUS,
                        it
                    )
                }
        }

        scope.launch {
            viewModel
                .normalNotification
                .map { makeServerStatusNotificationUseCase(this@ServerService, it) }
                .collect {
                    showNotificationUseCase(
                        this@ServerService,
                        NOTIFICATION_ID_SERVER_STATUS,
                        it
                    )
                }
        }

        scope.launch {
            presenter
                .finishEvents()
                .collect { stopSelf() }
        }

        presenter.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = when {
        // Intent == null means the Service was restarted by the system, assume this resumes the
        // server
        intent == null || intent.action == SERVER_SERVICE_ACTION_START -> {
            presenter.startServer()
            START_STICKY
        }

        intent.action == SERVER_SERVICE_ACTION_STOP -> {
            presenter.stopServerAndExit()
            START_NOT_STICKY
        }

        else -> throw IllegalArgumentException("Unexpected Intent action: ${intent.action}")
    }

    override fun onDestroy() {
        // Note for code reviewers:
        // You might notice that the server is stopped twice, here and onStartCommand.
        // There are two ways of stopping server. If it's within our control, it will be done
        // non-blocking on L55.
        //
        // And if onDestroy is called for any other reason beyond our control, we must ensure the
        // server is stopped so we have no choice except blocking the thread.
        //
        // This relies on the implementation that stopIfRunning does nothing if not running.
        presenter.stopServerBlocking()
        super.onDestroy()
    }
}
