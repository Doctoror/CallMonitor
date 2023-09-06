package com.dd.callmonitor.domain.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

class HandlePermissionUseCase(
    private val activity: Activity,
    private val permissionsRepository: PermissionsRepository
) {

    suspend operator fun invoke(
        permission: ApiLevelPermission,
        permissionRequestUseCase: RequestPermissionUseCase,
        whenGranted: () -> Unit,
        whenShouldShowRationale: () -> Unit,
        whenDenied: () -> Unit,
    ) {
        if (Build.VERSION.SDK_INT < permission.addedInSdkInt) {
            whenGranted()
            return
        }

        if (activity.checkSelfPermission(permission.permission) == PackageManager.PERMISSION_GRANTED) {
            whenGranted()
            return
        }

        if (activity.shouldShowRequestPermissionRationale(permission.permission)) {
            // If permission rationale was already shown this means the user either declined
            // it or already took action, never show it again and consider permission as denied
            if (permissionsRepository.wasPermissionRationaleShown(permission.permission)) {
                whenDenied()
                return
            }

            permissionsRepository.setPermissionRationaleShown(permission.permission, true)
            whenShouldShowRationale()
            return
        }

        permissionRequestUseCase()
    }
}
