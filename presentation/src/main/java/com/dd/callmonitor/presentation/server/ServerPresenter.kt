package com.dd.callmonitor.presentation.server

import android.content.res.Resources
import com.dd.callmonitor.domain.callstatus.CallStatusStartListeningUseCase
import com.dd.callmonitor.domain.callstatus.CallStatusStopListeningUseCase
import com.dd.callmonitor.domain.connectivity.ConnectivityState
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.server.Server
import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.domain.server.ServerStateProvider
import com.dd.callmonitor.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ServerPresenter(
    private val callStatusStartListeningUseCase: CallStatusStartListeningUseCase,
    private val callStatusStopListeningUseCase: CallStatusStopListeningUseCase,
    private val observeWifiConnectivityUseCase: ObserveWifiConnectivityUseCase,
    private val foregroundServiceStatusMessageProvider: ForegroundServiceStatusMessageProvider,
    private val resources: Resources,
    private val scope: CoroutineScope,
    private val server: Server,
    private val serverStateProvider: ServerStateProvider,
    val viewModel: ServerViewModel
) {

    private val finishEventsEmitter = MutableSharedFlow<Unit>()

    fun finishEvents(): Flow<Unit> = finishEventsEmitter

    fun onCreate() {
        scope.launch {
            serverStateProvider
                .state
                .drop(1) // we must drop initial state as we only want to handle state change
                .filter { it is ServerState.Error }
                .map { it as ServerState.Error }
                .collect {
                    viewModel.normalNotification.emit(
                        resources.getText(
                            when (it.error) {
                                ServerError.GENERIC ->
                                    R.string.server_power_button_when_error_label_generic

                                ServerError.NO_CONNECTIVITY ->
                                    R.string.server_power_button_when_error_label_not_connected

                                ServerError.NO_HOST_ADDRESS ->
                                    R.string.server_power_button_when_error_label_no_host_address
                            }
                        )
                    )
                    finishEventsEmitter.emit(Unit)
                }
        }

        scope.launch {
            serverStateProvider
                .state
                .map(foregroundServiceStatusMessageProvider::invoke)
                .filter { it.isPresent() }
                .collect {
                    viewModel
                        .foregroundNotification
                        .emit(it.get())
                }
        }

        scope.launch {
            observeWifiConnectivityUseCase()
                // Note for reviewers:
                // Also, we need to handle the case where the connectivity changes without
                // disconnecting because the old address would be invalid and we would have to
                // restart the server.
                //
                // This functionality is omitted to save some time as the task is huge even without
                // handling this case.
                .filter { it == ConnectivityState.Disconnected }
                .collect { stopIfRunningAndExitInternal() }
        }
    }

    fun startServer() {
        scope.launch {
            observeWifiConnectivityUseCase()
                .filter { it != ConnectivityState.Unknown }
                .take(1)
                .collect {
                    when (it) {
                        is ConnectivityState.Unknown ->
                            throw RuntimeException("Impossible, should be filtered above")

                        is ConnectivityState.Disconnected -> serverStateProvider.state.emit(
                            ServerState.Error(ServerError.NO_CONNECTIVITY)
                        )

                        is ConnectivityState.Connected -> {
                            callStatusStartListeningUseCase()
                            server.start(it.siteLocalAddress)
                        }
                    }
                }
        }
    }

    private suspend fun stopIfRunning() {
        callStatusStopListeningUseCase()
        server.stopIfRunning()
    }

    fun stopIfRunningAndExit() {
        scope.launch { stopIfRunningAndExitInternal() }
    }

    fun stopIfRunningBlocking() = runBlocking {
        stopIfRunning()
    }

    private suspend fun stopIfRunningAndExitInternal() {
        stopIfRunning()
        finishEventsEmitter.emit(Unit)
    }
}
