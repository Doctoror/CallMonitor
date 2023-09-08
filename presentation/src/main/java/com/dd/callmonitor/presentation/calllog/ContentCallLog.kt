package com.dd.callmonitor.presentation.calllog

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.dd.callmonitor.presentation.permissions.AutoAskPermissionEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContentCallLog(
    viewModel: CallLogViewModel,
    onReadCallLogPermissionGranted: () -> Unit,
) {

    val permissionState = rememberPermissionState(Manifest.permission.READ_CALL_LOG)
    AutoAskPermissionEffect(permissionState)

    when {
        permissionState.status.isGranted -> {
            LaunchedEffect(Unit) { onReadCallLogPermissionGranted() }
            ContentCallLogPermissionGranted(viewModel)
        }

        else -> ContentCallLogPermissionDenied(
            permissionState.status.shouldShowRationale,
            permissionState::launchPermissionRequest
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
