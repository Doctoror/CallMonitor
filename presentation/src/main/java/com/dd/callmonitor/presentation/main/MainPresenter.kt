package com.dd.callmonitor.presentation.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.dd.callmonitor.domain.connectivity.ConnectivityState
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.HandlePermissionUseCase
import com.dd.callmonitor.domain.permissions.RequestCallLogPermissionUseCase
import com.dd.callmonitor.domain.permissions.RequestPostNotificationsPermissionUseCase
import com.dd.callmonitor.domain.server.ServerStateProvider
import com.dd.callmonitor.domain.server.StartServerUseCase
import com.dd.callmonitor.domain.server.StopServerUseCase
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerStateUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MainPresenter(
    private val handlePermissionUseCase: HandlePermissionUseCase,
    private val observeWifiConnectivityUseCase: ObserveWifiConnectivityUseCase,
    private val requestCallLogPermissionUseCase: RequestCallLogPermissionUseCase,
    private val requestPostNotificationsPermissionUseCase: RequestPostNotificationsPermissionUseCase,
    private val serverStateProvider: ServerStateProvider,
    private val startServerUseCase: StartServerUseCase,
    private val stopServerUseCase: StopServerUseCase,
    private val updateViewModelOnServerStateUseCase: UpdateViewModelOnServerStateUseCase,
    val viewModel: MainViewModel
) : ViewModel() {

    fun onCreate(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                handlePermissionUseCase(
                    permission = ApiLevelPermissions.READ_CALL_LOG,
                    permissionRequestUseCase = requestCallLogPermissionUseCase,
                    whenGranted = ::onCallLogPermissionGranted,
                    whenShouldShowRationale = {
                        viewModel.showCallLogsPermissionRationale.value = true
                    },
                    whenDenied = ::onCallLogPermissionDenied
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onCallLogPermissionGranted() {
        viewModelScope.launch {
            observeWifiConnectivityUseCase()
                .flatMapLatest {
                    when (it) {
                        is ConnectivityState.Unknown -> {
                            flowOf()
                        }

                        is ConnectivityState.Disconnected -> {
                            viewModel.viewType.emit(MainViewModel.ViewType.NOT_CONNECTED)
                            flowOf()
                        }

                        is ConnectivityState.Connected -> {
                            serverStateProvider.state
                        }
                    }
                }
                .collect { updateViewModelOnServerStateUseCase(viewModel, it) }
        }
    }

    fun onCallLogPermissionDenied() {
        viewModel.viewType.value = MainViewModel.ViewType.CALL_LOGS_PERMISSION_DENIED
    }

    fun onCallLogPermissionRationaleDismiss() {
        viewModel.showCallLogsPermissionRationale.value = false
        onCallLogPermissionDenied()
    }

    fun onCallLogPermissionRationaleProceed() {
        viewModel.showCallLogsPermissionRationale.value = false
        requestCallLogPermissionUseCase()
    }

    /**
     * If permission rationale dismissed without requesting permission, consider this as if the
     * permission is denied.
     */
    fun onPostNotificationsPermissionRationaleDismiss() {
        viewModel.showNotificationPermissionRationale.value = false
        onPostNotificationsPermissionDenied()
    }

    fun onPostNotificationsPermissionRationaleProceed() {
        viewModel.showNotificationPermissionRationale.value = false
        requestPostNotificationsPermissionUseCase()
    }

    fun onPostNotificationsPermissionGranted() {
        startServerUseCase()
    }

    /**
     * If the POST_NOTIFICATIONS permission denied, proceed to start service without the permission.
     */
    fun onPostNotificationsPermissionDenied() {
        startServerUseCase()
    }

    fun onStopServerClick() {
        stopServerUseCase()
    }

    fun onStartServerClick() {
        viewModelScope.launch {
            handlePermissionUseCase(
                permission = ApiLevelPermissions.POST_NOTIFICATIONS,
                permissionRequestUseCase = requestPostNotificationsPermissionUseCase,
                whenGranted = ::onPostNotificationsPermissionGranted,
                whenShouldShowRationale = {
                    viewModel.showNotificationPermissionRationale.value = true
                },
                whenDenied = ::onPostNotificationsPermissionDenied,
            )
        }
    }
}
