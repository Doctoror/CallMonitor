package com.dd.callmonitor.domain.permissions

import androidx.activity.result.ActivityResultLauncher

class RequestCallLogPermissionUseCase(
    launcher: ActivityResultLauncher<String>,
) : RequestPermissionUseCase(
    launcher,
    ApiLevelPermissions.READ_CALL_LOG
)
