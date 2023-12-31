package com.dd.callmonitor.ui.calllog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.dd.callmonitor.ui.R

@Composable
fun ContentCallLogPermissionDenied(launchPermissionRequest: () -> Unit) {
    // Note for reviewers: scrolling here is needed because the button needs to be visible no matter
    // how small the screen is
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = stringResource(R.string.call_log_permission_denied_icon),
            modifier = Modifier.size(88.dp),
        )

        Text(
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            text = stringResource(R.string.call_log_permission_rationale_text),
            textAlign = TextAlign.Center
        )

        Button(onClick = launchPermissionRequest) {
            Text(stringResource(R.string.permission_rationale_proceed))
        }
    }
}

@Preview
@Composable
fun ContentCallLogPermissionDeniedPreview() {
    ContentCallLogPermissionDenied(
        launchPermissionRequest = {}
    )
}
