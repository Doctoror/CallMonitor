package com.dd.callmonitor.domain.permissions

import androidx.activity.result.ActivityResultLauncher

class RequestPostNotificationsPermissionUseCase(
    launcher: ActivityResultLauncher<String>,
) : RequestPermissionUseCase(
    launcher,
    ApiLevelPermissions.POST_NOTIFICATIONS
)
