package com.dd.callmonitor.presentation.main.usecases

import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsViewModel

class ServerStateViewModelUpdater(
    private val serverErrorViewModelUpdater: ServerErrorViewModelUpdater,
    private val updateViewModelOnServerIdleUseCase: ServerIdleViewModelUpdater,
    private val serverInitializingViewModelUpdater: ServerInitializingViewModelUpdater,
    private val serverRunningViewModelUpdater: ServerRunningViewModelUpdater,
    private val serverStoppingViewModelUpdater: ServerStoppingViewModelUpdater
) {

    operator fun invoke(viewModel: ServerControlsViewModel, state: ServerState) {
        when (state) {
            is ServerState.Error ->
                serverErrorViewModelUpdater(viewModel, state.error)

            is ServerState.Idle ->
                updateViewModelOnServerIdleUseCase(viewModel)

            is ServerState.Initialising ->
                serverInitializingViewModelUpdater(viewModel)

            is ServerState.Running ->
                serverRunningViewModelUpdater(viewModel, state.host, state.port)

            is ServerState.Stopping ->
                serverStoppingViewModelUpdater(viewModel)
        }
    }
}
