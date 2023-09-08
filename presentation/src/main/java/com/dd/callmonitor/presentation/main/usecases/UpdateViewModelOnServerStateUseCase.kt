package com.dd.callmonitor.presentation.main.usecases

import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsViewModel

class UpdateViewModelOnServerStateUseCase(
    private val updateViewModelOnServerErrorUseCase: UpdateViewModelOnServerErrorUseCase,
    private val updateViewModelOnServerIdleUseCase: UpdateViewModelOnServerIdleUseCase,
    private val updateViewModelOnServerInitializingUseCase: UpdateViewModelOnServerInitializingUseCase,
    private val updateViewModelOnServerRunningUseCase: UpdateViewModelOnServerRunningUseCase,
    private val updateViewModelOnServerStoppingUseCase: UpdateViewModelOnServerStoppingUseCase
) {

    operator fun invoke(viewModel: ServerControlsViewModel, state: ServerState) {
        when (state) {
            is ServerState.Error ->
                updateViewModelOnServerErrorUseCase(viewModel, state.error)

            is ServerState.Idle ->
                updateViewModelOnServerIdleUseCase(viewModel)

            is ServerState.Initialising ->
                updateViewModelOnServerInitializingUseCase(viewModel)

            is ServerState.Running ->
                updateViewModelOnServerRunningUseCase(viewModel, state.host, state.port)

            is ServerState.Stopping ->
                updateViewModelOnServerStoppingUseCase(viewModel)
        }
    }
}
