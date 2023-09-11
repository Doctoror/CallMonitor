package com.dd.callmonitor.app.components.server

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import com.dd.callmonitor.app.components.CoroutineScopeService
import com.dd.callmonitor.app.notifications.NOTIFICATION_ID_SERVER_STATUS
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.presentation.server.ServerPresenter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

const val SERVER_SERVICE_ACTION_START = "ACTION_START"
const val SERVER_SERVICE_ACTION_STOP = "ACTION_STOP"

class ServerService : CoroutineScopeService() {

    private val checkPermissionUseCase: CheckPermissionUseCase by inject()

    private val serverStatusNotificationProvider: ServerStatusNotificationProvider by inject()

    private val presenter: ServerPresenter by inject {
        parametersOf(scope)
    }

    private val viewModel by lazy { presenter.viewModel }

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            viewModel
                .foregroundNotification
                .map { serverStatusNotificationProvider.provide(this@ServerService, it) }
                .collect(::startForegroundCompat)
        }

        scope.launch {
            viewModel
                .normalNotification
                .map { serverStatusNotificationProvider.provide(this@ServerService, it) }
                .collect(::showNotification)
        }

        scope.launch {
            presenter
                .finishEvents()
                .collect { stopSelf() }
        }

        presenter.onCreate()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(notification: Notification) {
        checkPermissionUseCase(
            permission = ApiLevelPermissions.POST_NOTIFICATIONS,
            whenDenied = {},
            whenGranted = {
                NotificationManagerCompat
                    .from(this@ServerService)
                    .notify(NOTIFICATION_ID_SERVER_STATUS, notification)
            }
        )
    }

    private fun startForegroundCompat(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID_SERVER_STATUS,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(
                NOTIFICATION_ID_SERVER_STATUS,
                notification
            )
        }
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
            presenter.stopIfRunningAndExit()
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
        presenter.stopIfRunningBlocking()
        super.onDestroy()
    }
}
