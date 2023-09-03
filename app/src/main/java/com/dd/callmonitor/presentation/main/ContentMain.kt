package com.dd.callmonitor.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dd.callmonitor.R
import com.dd.callmonitor.presentation.theme.CallMonitorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentMain(
    onPermissionRationaleDismiss: () -> Unit,
    onPermissionRationaleProceed: () -> Unit,
    onStartServerClick: () -> Unit,
    onStopServerClick: () -> Unit,
    viewModel: MainViewModel
) {
    val powerButtonAction = viewModel.powerButtonAction
        .collectAsStateWithLifecycle()

    val powerButtonContentDescription = viewModel.powerButtonContentDescription
        .collectAsStateWithLifecycle()

    val powerButtonLabel = viewModel.powerButtonLabel
        .collectAsStateWithLifecycle()

    val powerButtonLoading = viewModel.powerButtonLoading
        .collectAsStateWithLifecycle()

    val powerButtonTint = viewModel.powerButtonTint
        .collectAsStateWithLifecycle()

    val showNotificationPermissionRationaleDialog = viewModel
        .showNotificationPermissionRationaleDialog
        .collectAsStateWithLifecycle()

    val viewType = viewModel.viewType
        .collectAsStateWithLifecycle()

    CallMonitorTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                when (viewType.value) {
                    MainViewModel.ViewType.LOADING -> ContentMainLoading()

                    MainViewModel.ViewType.SERVER_CONTROLS -> ContentPowerButtonWithLabel(
                        isLoading = powerButtonLoading.value,
                        label = powerButtonLabel.value,
                        tint = powerButtonTint.value,
                        onClick = when (powerButtonAction.value) {
                            MainViewModel.PowerButtonAction.NONE -> null
                            MainViewModel.PowerButtonAction.START -> onStartServerClick
                            MainViewModel.PowerButtonAction.STOP -> onStopServerClick
                        },
                        onClickLabel = powerButtonContentDescription.value
                    )
                }
            }
        }

        if (showNotificationPermissionRationaleDialog.value) {
            ContentNotificationPermissionRationaleDialog(
                onDismiss = onPermissionRationaleDismiss,
                onProceed = onPermissionRationaleProceed
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainContentPreviewLoading() {
    ContentMain(
        onPermissionRationaleDismiss = {},
        onPermissionRationaleProceed = {},
        onStartServerClick = {},
        onStopServerClick = {},
        viewModel = MainViewModel().apply {
            viewType.value = MainViewModel.ViewType.LOADING
        }
    )
}

@Composable
@Preview(showBackground = true)
fun MainContentPreviewServerIdle() {
    ContentMain(
        onPermissionRationaleDismiss = {},
        onPermissionRationaleProceed = {},
        onStartServerClick = {},
        onStopServerClick = {},
        viewModel = MainViewModel().apply {
            powerButtonLabel.value = stringResource(R.string.server_power_button_when_idle_label)
            powerButtonTint.value = Color.Red
            viewType.value = MainViewModel.ViewType.SERVER_CONTROLS
        }
    )
}

@Composable
@Preview(showBackground = true)
fun MainContentPreviewServerInitializing() {
    ContentMain(
        onPermissionRationaleDismiss = {},
        onPermissionRationaleProceed = {},
        onStartServerClick = {},
        onStopServerClick = {},
        viewModel = MainViewModel().apply {
            powerButtonLoading.value = true
            powerButtonLabel.value =
                stringResource(R.string.server_power_button_when_initializing_label)
            viewType.value = MainViewModel.ViewType.SERVER_CONTROLS
        }
    )
}
