package com.dd.callmonitor.presentation.calllog

import android.content.res.Resources
import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.R
import java.util.Locale
import java.util.concurrent.TimeUnit

class CallLogEntryViewModelMapper(
    private val locale: Locale,
    private val resources: Resources
) {

    fun map(entry: CallLogEntry) = CallLogEntryViewModel(
        duration = formatDuration(entry.durationSeconds),
        name = entry.name.ifEmpty { resources.getString(R.string.call_log_stub_unknown) }
    )

    private fun formatDuration(totalSeconds: Long): String {
        val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds)
        val seconds = totalSeconds - TimeUnit.MINUTES.toSeconds(minutes)

        return String.format(locale, "%02d:%02d", minutes, seconds)
    }
}
