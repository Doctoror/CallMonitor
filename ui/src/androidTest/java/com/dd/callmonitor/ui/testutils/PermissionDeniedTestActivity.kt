package com.dd.callmonitor.ui.testutils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity

class PermissionDeniedTestActivity : ComponentActivity() {

    override fun checkPermission(permission: String, pid: Int, uid: Int) =
        PackageManager.PERMISSION_DENIED
}
