package com.dd.callmonitor.ui.calllog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dd.callmonitor.ui.R

const val TEST_TAG_CALL_LOG_FAILURE_ICON = "TEST_TAG_CALL_LOG_FAILURE_ICON"

@Composable
fun ContentCallLogFailure() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            // Note for reviewers, no need for contentDescription because it would be same as the
            // text below.
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(88.dp)
                .testTag(TEST_TAG_CALL_LOG_FAILURE_ICON)
        )

        Text(
            text = stringResource(R.string.call_log_failure),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ContentCallLogFailurePreview() {
    ContentCallLogFailure()
}
