package com.dd.callmonitor.presentation.calllog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
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
fun ContentCallLogEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CallEnd,
            // Note for reviewers, no need for contentDescription because it would be same as the
            // text below.
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(88.dp),
        )

        Text(
            text = stringResource(R.string.call_log_empty),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ContentCallLogEmptyPreview() {
    ContentCallLogEmpty()
}
