package com.dd.callmonitor.presentation.main

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dd.callmonitor.presentation.R

@Composable
fun BoxScope.ContentMainCallLogsPermissionDenied() {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 56.dp
            )
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = stringResource(R.string.call_logs_permission_denied_icon),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(88.dp),
        )

        Text(
            text = stringResource(R.string.call_logs_permission_denied_message),
            textAlign = TextAlign.Center
        )
    }
}
