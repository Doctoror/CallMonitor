package com.dd.callmonitor.app.components.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.presentation.calllog.CallLogPresenter
import com.dd.callmonitor.presentation.server.externalcontrols.ServerControlsPresenter
import com.dd.callmonitor.ui.main.ContentMain
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        onPermissionsProcessed()
    }

    private val callLogPresenter: CallLogPresenter by viewModel()

    private val callLogsViewModel by lazy { callLogPresenter.viewModel }

    private val serverControlsPresenter: ServerControlsPresenter by viewModel()

    private val serverControlsViewModel by lazy { serverControlsPresenter.viewModel }

    private val serverPermissions by lazy {
        arrayOf(
            ApiLevelPermissions.POST_NOTIFICATIONS,
            ApiLevelPermissions.READ_CALL_LOG,
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
                onApplicationSettingsClick = ::launchApplicationSettings,
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
        requestPermissionsLauncher.launch(serverPermissions)
    }

    /**
     * Regardless if has any permissions, start the server. It will serve error responses for
     * services that have permissions denied.
     */
    private fun onPermissionsProcessed() {
        serverControlsPresenter.startServer()
    }

    private fun launchApplicationSettings() {
        // Note for reviewers: yes, intended to crash on ActivityNotFoundException.
        // I'd rather know if it's possible and if so, the UI needs to be adjusted to not show the
        // button in first place.
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
        )
    }
}
