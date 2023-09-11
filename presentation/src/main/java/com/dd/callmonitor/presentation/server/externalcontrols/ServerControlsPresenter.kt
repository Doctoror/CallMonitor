package com.dd.callmonitor.presentation.server.externalcontrols


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dd.callmonitor.domain.connectivity.ConnectivityState
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.server.ServerStateProvider
import com.dd.callmonitor.domain.server.StartServerUseCase
import com.dd.callmonitor.domain.server.StopServerUseCase
import com.dd.callmonitor.presentation.server.externalcontrols.updaters.ServerStateViewModelUpdater
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class ServerControlsPresenter(
    private val observeWifiConnectivityUseCase: ObserveWifiConnectivityUseCase,
    private val serverStateProvider: ServerStateProvider,
    private val startServerUseCase: StartServerUseCase,
    private val stopServerUseCase: StopServerUseCase,
    private val serverStateViewModelUpdater: ServerStateViewModelUpdater,
    val viewModel: ServerControlsViewModel
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onCreate() {
        viewModelScope.launch {
            observeWifiConnectivityUseCase()
                .flatMapLatest {
                    when (it) {
                        is ConnectivityState.Unknown -> {
                            // Do nothing and wait for next emission
                            flowOf()
                        }

                        is ConnectivityState.Disconnected -> {
                            viewModel.viewType.emit(ServerControlsViewModel.ViewType.NO_CONNECTION)
                            flowOf()
                        }

                        is ConnectivityState.Connected -> {
                            serverStateProvider.state
                        }
                    }
                }
                .collect { serverStateViewModelUpdater(viewModel, it) }
        }
    }

    fun onStopServerClick() {
        stopServerUseCase()
    }

    fun startServer() {
        startServerUseCase()
    }
}
