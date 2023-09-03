package com.dd.callmonitor.domain.notifications

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import com.dd.callmonitor.presentation.main.MainViewModel

// TODO REMOVE ME?
class HandleNotificationPermissionAndContinueUseCase(
    private val checkPostNotificationsPermissionUseCase: CheckPostNotificationsPermissionUseCase,
    private val showNotificationPermissionRationaleUseCase: ShowNotificationPermissionRationaleUseCase,
    private val viewModel: MainViewModel
) {

    /**
     * Asks for the permission if necessary or shows rationale, invokes [whenDone] if
     * - permission was already granted
     * - permission requested and granted
     * - permission requested and denied
     *
     * Does not invoke [whenDone] if
     * - the permission rationale was shown
     */
    operator fun invoke(activity: Activity, whenDone: () -> Unit) {
        if (checkPostNotificationsPermissionUseCase(activity) == PackageManager.PERMISSION_GRANTED) {
            whenDone()
        } else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            viewModel.showNotificationPermissionRationaleDialog.value = true
        } else {
            whenDone()
        }
    }
}
