package com.dd.callmonitor.domain.permissions

import android.Manifest
import android.os.Build

object ApiLevelPermissions {

    val POST_NOTIFICATIONS = ApiLevelPermission(
        Build.VERSION_CODES.TIRAMISU,
        "android.permission.POST_NOTIFICATIONS"
    )

    val READ_CALL_LOG = ApiLevelPermission(
        Build.VERSION_CODES.JELLY_BEAN,
        Manifest.permission.READ_CALL_LOG
    )

    val READ_PHONE_NUMBERS = ApiLevelPermission(
        Build.VERSION_CODES.O,
        "android.permission.READ_PHONE_NUMBERS"
    )

    val READ_PHONE_STATE = ApiLevelPermission(
        Build.VERSION_CODES.BASE,
        Manifest.permission.READ_PHONE_STATE
    )
}
