package com.dd.callmonitor.app.components.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.presentation.calllog.CallLogPresenter
import com.dd.callmonitor.presentation.main.ContentMain
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsPresenter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        onPermissionsProcessed()
    }

    private val callLogPresenter: CallLogPresenter by inject()

    private val callLogsViewModel by lazy { callLogPresenter.viewModel }

    private val serverControlsPresenter: ServerControlsPresenter by inject()

    private val serverControlsViewModel by lazy { serverControlsPresenter.viewModel }

    private val serverPermissions by lazy {
        arrayOf(
            ApiLevelPermissions.POST_NOTIFICATIONS,
            ApiLevelPermissions.READ_CONTACTS,
            ApiLevelPermissions.READ_PHONE_STATE,
            ApiLevelPermissions.READ_PHONE_NUMBERS,
        ).filter {
            Build.VERSION.SDK_INT >= it.addedInSdkInt
        }.map {
            it.permission
        }.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentMain(
                callLogViewModel = callLogsViewModel,
                serverControlsViewModel = serverControlsViewModel,
                onReadCallLogPermissionGranted = callLogPresenter::onReadCallLogPermissionGranted,
                onStartServerClick = ::onStartServerClick,
                onStopServerClick = serverControlsPresenter::onStopServerClick
            )
        }

        serverControlsPresenter.onCreate()
    }

    /**
     * Always requests all the permissions.
     *
     * It is okay to request multiple times as the system will ultimately decide what to deny and
     * when to stop asking again.
     *
     * A permission rationale is completely ignored here, I had this implemented at some stage of
     * this project, but for all 3 permissions it resulted in a callback hell, and we also wouldn't
     * be able to use [ActivityResultContracts.RequestMultiplePermissions] if we had to manage
     * rationale for each permission.
     *
     * Also, READ_PHONE_STATE is not required before API level 31, but anyway, READ_PHONE_NUMBERS
     * and READ_PHONE_STATE are both the same "Phone" permission on user-facing UI, so it doesn't
     * hurt to just request both instead of introducing more logic.
     *
     * Also, READ_PHONE_NUMBERS is not really required before API level 29, so we could also add an
     * additional filter, but that also seems like complicating the code unnecessarily.
     */
    private fun onStartServerClick() {
        lifecycleScope.launch {
            requestPermissionsLauncher.launch(serverPermissions)
        }
    }

    /**
     * Regardless if has any permissions, start the server. It will serve error responses for
     * services that have permissions denied.
     */
    private fun onPermissionsProcessed() {
        serverControlsPresenter.startServer()
    }
}
