package com.dd.callmonitor.data.server.routes.log

import com.dd.callmonitor.data.server.routes.root.ResponseTimeFormatter
import com.dd.callmonitor.domain.calllog.CallLogEntry

internal class CallLogResponseMapper(
    private val responseTimeFormatter: ResponseTimeFormatter
) {

    fun map(entry: CallLogEntry) = CallLogResponseEntry(
        beginning = responseTimeFormatter.format(entry.beginningMillisUtc),
        duration = entry.durationSeconds.toString(),
        number = entry.number.orElse(null),
        name = entry.name.orElse(null),
        timesQueried = entry.timesQueried
    )
}
