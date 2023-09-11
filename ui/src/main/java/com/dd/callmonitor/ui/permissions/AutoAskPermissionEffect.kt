package com.dd.callmonitor.ui.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

/**
 * A way to automatically launch permission without needing to save a persistent flag that indicates
 * whether the permission was already asked.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AutoAskPermissionEffect(permissionState: MultiplePermissionsState) {
    LaunchedEffect(permissionState.permissions) {
        if (permissionState.permissions.find { it.status.shouldAskForPermission() } != null) {
            permissionState.launchMultiplePermissionRequest()
        }
    }
}

/**
 * If the permission is not granted and rationale not shown it is safe to assume we can ask for
 * a permission because either it was never asked before, or it was already denied twice. And if
 * it was denied twice this request will be ignored by the system anyway (or user can choose
 * "never ask again" on older platforms).
 */
@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionStatus.shouldAskForPermission() = !isGranted && !shouldShowRationale
