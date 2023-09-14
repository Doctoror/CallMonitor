package com.dd.callmonitor.ui.testutils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity

/**
 * This is here instead of relying on [androidx.test.rule.GrantPermissionRule], which permanently
 * affects global state of the test app. With GrantPermissionRule the order of test execution
 * matters and once the permission was granted by a test, a next test cannot un-grant it.
 *
 * So instead of using GrantPermissionRule, Activities that hardcode permission state are used.
 */
class PermissionGrantedTestActivity : ComponentActivity() {

    override fun checkPermission(permission: String, pid: Int, uid: Int) =
        PackageManager.PERMISSION_GRANTED
}
