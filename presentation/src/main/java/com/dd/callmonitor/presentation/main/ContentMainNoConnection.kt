package com.dd.callmonitor.presentation.main

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
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
fun BoxScope.ContentMainNoConnection() {
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
            imageVector = Icons.Default.WifiOff,
            // Note for code reviewers: no need for any label here, as it would repeat the label
            // below
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(88.dp),
        )

        Text(
            text = stringResource(R.string.not_connected_to_wifi_message),
            textAlign = TextAlign.Center
        )
    }
}
