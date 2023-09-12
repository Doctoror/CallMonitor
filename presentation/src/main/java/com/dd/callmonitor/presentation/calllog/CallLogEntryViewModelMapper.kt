package com.dd.callmonitor.presentation.calllog

import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.contacts.TransformEmptyContactNameUseCase
import java.util.Locale
import java.util.concurrent.TimeUnit

class CallLogEntryViewModelMapper(
    private val locale: Locale,
    private val transformEmptyContactNameUseCase: TransformEmptyContactNameUseCase
) {

    fun map(entry: CallLogEntry) = CallLogEntryViewModel(
        duration = formatDuration(entry.durationSeconds),
        name = transformEmptyContactNameUseCase(entry.name)
    )

    private fun formatDuration(totalSeconds: Long): String {
        val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds)
        val seconds = totalSeconds - TimeUnit.MINUTES.toSeconds(minutes)

        return String.format(locale, "%02d:%02d", minutes, seconds)
    }
}
