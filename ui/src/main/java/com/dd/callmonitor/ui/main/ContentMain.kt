package com.dd.callmonitor.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dd.callmonitor.presentation.calllog.CallLogViewModel
import com.dd.callmonitor.presentation.server.externalcontrols.ServerControlsViewModel
import com.dd.callmonitor.ui.R
import com.dd.callmonitor.ui.calllog.ContentCallLog
import com.dd.callmonitor.ui.server.externalcontrols.ContentServerControls
import com.dd.callmonitor.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentMain(
    callLogViewModel: CallLogViewModel,
    serverControlsViewModel: ServerControlsViewModel,
    onApplicationSettingsClick: () -> Unit,
    onReadCallLogPermissionGranted: () -> Unit,
    onStartServerClick: () -> Unit,
    onStopServerClick: () -> Unit,
) {
    AppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                ) {

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        ContentServerControls(
                            onStartServerClick,
                            onStopServerClick,
                            serverControlsViewModel
                        )
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        color = AppTheme.colorScheme.onSurfaceVariant,
                        style = AppTheme.typography.titleLarge,
                        text = stringResource(R.string.call_log_header)
                    )

                    ContentCallLog(
                        viewModel = callLogViewModel,
                        onApplicationSettingsClick = onApplicationSettingsClick,
                        onReadCallLogPermissionGranted = onReadCallLogPermissionGranted,
                        shouldAutoAskForPermissions = true
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainContentPreview() {
    ContentMain(
        callLogViewModel = CallLogViewModel(),
        serverControlsViewModel = ServerControlsViewModel(),
        onApplicationSettingsClick = {},
        onReadCallLogPermissionGranted = {},
        onStartServerClick = {},
        onStopServerClick = {},
    )
}
