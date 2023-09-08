package com.dd.callmonitor.presentation.permissions

import android.app.Activity
import android.content.pm.PackageManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

// TODO remove me
@OptIn(ExperimentalPermissionsApi::class)
object ShouldAskForPermissionChecker {

    fun shouldAskForPermission(activity: Activity, permission: String) =
        shouldAskForPermission(obtainPermissionStatus(activity, permission))

    private fun obtainPermissionStatus(
        activity: Activity,
        permission: String
    ) = if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
        PermissionStatus.Granted
    } else {
        PermissionStatus.Denied(activity.shouldShowRequestPermissionRationale(permission))
    }

    /**
     * If the permission is not granted and rationale not shown it is safe to assume we can ask for
     * a permission because either it was never asked before, or it was already denied twice. And if
     * it was denied twice this request will be ignored by the system anyway.
     */
    fun shouldAskForPermission(status: PermissionStatus) =
        !status.isGranted && !status.shouldShowRationale
}
