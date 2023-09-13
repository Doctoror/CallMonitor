package com.dd.callmonitor.data.server.routes.log

import com.dd.callmonitor.data.server.routes.root.ResponseTimeFormatter
import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.contacts.TransformEmptyContactNameUseCase
import com.dd.callmonitor.domain.contacts.TransformEmptyPhoneNumberUseCase

internal class CallLogResponseMapper(
    private val responseTimeFormatter: ResponseTimeFormatter,
    private val transformEmptyContactNameUseCase: TransformEmptyContactNameUseCase,
    private val transformEmptyPhoneNumberUseCase: TransformEmptyPhoneNumberUseCase
) {

    fun map(entry: CallLogEntry) = CallLogResponseEntry(
        beginning = responseTimeFormatter.format(entry.beginningMillisUtc),
        duration = entry.durationSeconds.toString(),
        number = transformEmptyPhoneNumberUseCase(entry.number),
        name = transformEmptyContactNameUseCase(entry.name),
        timesQueried = entry.timesQueried
    )
}
