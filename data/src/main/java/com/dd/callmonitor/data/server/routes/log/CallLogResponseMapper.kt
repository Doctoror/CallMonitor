package com.dd.callmonitor.data.server.routes.log

import com.dd.callmonitor.data.server.routes.root.ResponseTimeFormatter
import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.contacts.TransformEmptyContactNameUseCase

internal class CallLogResponseMapper(
    private val responseTimeFormatter: ResponseTimeFormatter,
    private val transformEmptyContactNameUseCase: TransformEmptyContactNameUseCase
) {

    fun map(entry: CallLogEntry) = CallLogResponseEntry(
        beginning = responseTimeFormatter.format(entry.beginningMillisUtc),
        duration = entry.durationSeconds.toString(),
        number = entry.number,
        name = transformEmptyContactNameUseCase(entry.name),
        timesQueried = entry.timesQueried
    )
}
