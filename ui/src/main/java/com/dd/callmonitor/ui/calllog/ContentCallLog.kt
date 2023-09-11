package com.dd.callmonitor.ui.calllog

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.dd.callmonitor.presentation.calllog.CallLogEntryViewModel
import com.dd.callmonitor.presentation.calllog.CallLogViewModel
import com.dd.callmonitor.ui.permissions.AutoAskPermissionEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContentCallLog(
    viewModel: CallLogViewModel,
    onReadCallLogPermissionGranted: () -> Unit,
) {

    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
        )
    )
    AutoAskPermissionEffect(permissionState)

    val callLogPermissionState = permissionState.permissions.first {
        it.permission == Manifest.permission.READ_CALL_LOG
    }

    when {
        callLogPermissionState.status.isGranted -> {
            LaunchedEffect(Unit) { onReadCallLogPermissionGranted() }
            ContentCallLogPermissionGranted(viewModel)
        }

        else -> ContentCallLogPermissionDenied(
            callLogPermissionState.status.shouldShowRationale,
            callLogPermissionState::launchPermissionRequest
        )
    }
}

@Preview
@Composable
fun ContentCallLogPermissionGrantedAndLoadedPreview() {
    ContentCallLog(
        viewModel = CallLogViewModel().apply {
            callLog.value = listOf(
                CallLogEntryViewModel(
                    duration = "01:13",
                    name = "Someone"
                ),
                CallLogEntryViewModel(
                    duration = "12:13",
                    name = "Contact"
                )
            )
        },
        onReadCallLogPermissionGranted = {},
    )
}
