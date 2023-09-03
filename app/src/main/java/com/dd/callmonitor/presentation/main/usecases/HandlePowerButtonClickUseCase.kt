package com.dd.callmonitor.presentation.main.usecases

import com.dd.callmonitor.data.server.ServerState

// TODO REMOVE ME
class HandlePowerButtonClickUseCase(
    private val startServerUseCase: StartServerUseCase,
    private val stopServerUseCase: StopServerUseCase
) {

    operator fun invoke(state: ServerState) {
        when (state) {
            is ServerState.Idle,
            ServerState.Error -> startServerUseCase()

            is ServerState.Initialising -> Unit
            is ServerState.Running -> stopServerUseCase()
            is ServerState.Stopping -> Unit
        }
    }
}
