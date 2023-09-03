package com.dd.callmonitor.presentation.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import com.dd.callmonitor.data.notifications.NotificationPreferencesRepository
import com.dd.callmonitor.data.server.ServerStateProvider
import com.dd.callmonitor.domain.notifications.CheckPostNotificationsPermissionUseCase
import com.dd.callmonitor.domain.notifications.RequestNotificationsPermissionUseCase
import com.dd.callmonitor.framework.lifecycle.LifecycleViewModelPresenter
import com.dd.callmonitor.presentation.main.usecases.StartServerUseCase
import com.dd.callmonitor.presentation.main.usecases.StopServerUseCase
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerStateUseCase
import kotlinx.coroutines.launch

class MainPresenter(
    private val checkPostNotificationsPermissionUseCase: CheckPostNotificationsPermissionUseCase,
    private val notificationPreferencesRepository: NotificationPreferencesRepository,
    private val requestNotificationsPermissionUseCase: RequestNotificationsPermissionUseCase,
    private val serverStateProvider: ServerStateProvider,
    private val startServerUseCase: StartServerUseCase,
    private val stopServerUseCase: StopServerUseCase,
    private val updateViewModelOnServerStateUseCase: UpdateViewModelOnServerStateUseCase,
    val viewModel: MainViewModel
) : LifecycleViewModelPresenter() {

    override fun onCreate() {
        viewModelScope.launch {
            serverStateProvider
                .state
                .collect { updateViewModelOnServerStateUseCase(viewModel, it) }
        }
    }

    /**
     * If permission rationale dismissed without requesting permission, consider this as if the
     * permission is denied.
     */
    fun onPostNotificationsPermissionRationaleDismiss() {
        viewModel.showNotificationPermissionRationaleDialog.value = false
        onPostNotificationsPermissionDenied()
    }

    fun onPostNotificationsPermissionRationaleProceed() {
        viewModel.showNotificationPermissionRationaleDialog.value = false
        requestNotificationsPermissionUseCase()
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

    fun onStartServerClick(activity: Activity) {
        if (checkPostNotificationsPermissionUseCase(activity) == PackageManager.PERMISSION_GRANTED) {
            onPostNotificationsPermissionGranted()
            return
        }

        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            viewModelScope.launch {
                // If permission rationale was already shown this means the user either declined
                // it or already took action, never show it again and consider permission as denied
                if (notificationPreferencesRepository.wasPermissionRationaleShown()) {
                    onPostNotificationsPermissionDenied()
                } else {
                    viewModel.showNotificationPermissionRationaleDialog.emit(true)
                    notificationPreferencesRepository.setPermissionRationaleShown(true)
                }
            }
        } else {
            requestNotificationsPermissionUseCase()
        }
    }
}
