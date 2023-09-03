package com.dd.callmonitor.data.server

import android.content.Intent
import android.os.IBinder
import com.dd.callmonitor.domain.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.notifications.NOTIFICATION_ID_SERVER_STATUS
import com.dd.callmonitor.domain.server.MakeForegroundServerStatusNotificationUseCase
import com.dd.callmonitor.domain.server.ShowServerFailedNotificationUseCase
import com.dd.callmonitor.domain.server.StartForegroundServiceUseCase
import com.dd.callmonitor.framework.components.CoroutineScopeService
import com.dd.callmonitor.framework.lifecycle.ActivityMonitor
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

const val SERVER_SERVICE_ACTION_START = "ACTION_START"
const val SERVER_SERVICE_ACTION_STOP = "ACTION_STOP"

class ServerService : CoroutineScopeService() {

    private val activityMonitor: ActivityMonitor by inject()

    private val makeForegroundServerStatusNotificationUseCase: MakeForegroundServerStatusNotificationUseCase by inject()

    private val observeWifiConnectivityUseCase: ObserveWifiConnectivityUseCase by inject()

    private val server: Server by inject()

    private val serverStateProvider: ServerStateProvider by inject()

    private val showServerFailedNotificationUseCase: ShowServerFailedNotificationUseCase by inject()

    private val startForegroundServiceUseCase: StartForegroundServiceUseCase by inject()

    override fun onCreate() {
        super.onCreate()

        scope.launch {
            serverStateProvider
                .state
                .drop(1) // we must drop initial state as we only want to handle state change
                .filter { it == ServerState.Error }
                .collect {
                    if (activityMonitor.getNumberOfStartedActivities() == 0) {
                        showServerFailedNotificationUseCase(this@ServerService)
                    }
                    stopSelf()
                }
        }

        scope.launch {
            serverStateProvider
                .state
                .map { makeForegroundServerStatusNotificationUseCase(this@ServerService, it) }
                .filter { it.isPresent() }
                .collect {
                    startForegroundServiceUseCase(
                        this@ServerService,
                        NOTIFICATION_ID_SERVER_STATUS,
                        it.get()
                    )
                }
        }

        scope.launch {
            observeWifiConnectivityUseCase()
                .filter { it.isPresent() && !it.get() }
                .collect { stopServerAndSelf() }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = when {
        // Intent == null means the Service was restarted by the system, assume this resumes the
        // server
        intent == null || intent.action == SERVER_SERVICE_ACTION_START -> {
            scope.launch {
                server.start()
            }
            START_STICKY
        }

        intent.action == SERVER_SERVICE_ACTION_STOP -> {
            scope.launch {
                stopServerAndSelf()
            }
            START_NOT_STICKY
        }

        else -> throw IllegalArgumentException("Unexpected Intent action: ${intent.action}")
    }

    private suspend fun stopServerAndSelf() {
        server.stopIfRunning()
        stopSelf()
    }

    override fun onDestroy() {
        // Note for code reviewers:
        // You might notice that the server is stopped twice, here and on L55.
        // There are two ways of stopping server. If it's within our control, it will be done
        // non-blocking on L55.
        //
        // And if onDestroy is called for any other reason beyond our control, we must ensure the
        // server is stopped so we have no choice except blocking the thread.
        //
        // This relies on the implementation that stopIfRunning does nothing if not running.
        runBlocking { server.stopIfRunning() }
        super.onDestroy()
    }
}
