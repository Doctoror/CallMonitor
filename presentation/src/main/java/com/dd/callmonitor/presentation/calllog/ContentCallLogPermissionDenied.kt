package com.dd.callmonitor.presentation.calllog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dd.callmonitor.presentation.R

@Composable
fun ContentCallLogPermissionDenied(
    canRequest: Boolean,
    launchPermissionRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = stringResource(R.string.call_log_permission_denied_icon),
            modifier = Modifier.size(88.dp),
        )

        Text(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 24.dp
                ),
            text = stringResource(R.string.call_log_permission_rationale_text),
            textAlign = TextAlign.Center
        )

        // TODO If we can no longer request we might still show the button and redirect to
        // Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
        if (canRequest) {
            Button(onClick = launchPermissionRequest) {
                Text(stringResource(R.string.permission_rationale_proceed))
            }
        }
    }
}

@Preview
@Composable
fun ContentCallLogPermissionDeniedPreviewCannotRequest() {
    ContentCallLogPermissionDenied(
        canRequest = false,
        launchPermissionRequest = {}
    )
}

@Preview
@Composable
fun ContentCallLogPermissionDeniedPreviewCanRequest() {
    ContentCallLogPermissionDenied(
        canRequest = true,
        launchPermissionRequest = {}
    )
}
