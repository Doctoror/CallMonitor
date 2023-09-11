package com.dd.callmonitor.ui.calllog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dd.callmonitor.presentation.calllog.CallLogEntryViewModel
import com.dd.callmonitor.presentation.calllog.CallLogViewModel
import com.dd.callmonitor.ui.theme.AppTheme

@Composable
fun ContentCallLogLoaded(viewModel: CallLogViewModel) {
    val entries = viewModel.callLog.collectAsStateWithLifecycle()

    if (entries.value.isEmpty()) {
        ContentCallLogEmpty()
    } else {
        LazyColumn {
            items(entries.value) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 72.dp),
                ) {
                    Text(
                        color = AppTheme.colorScheme.onSurface,
                        style = AppTheme.typography.bodyLarge,
                        text = it.name
                    )
                    Text(
                        color = AppTheme.colorScheme.onSurfaceVariant,
                        style = AppTheme.typography.bodyMedium,
                        text = it.duration
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ContentCallLogLoadedPreview() {
    ContentCallLogLoaded(CallLogViewModel().apply {
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
    })
}
