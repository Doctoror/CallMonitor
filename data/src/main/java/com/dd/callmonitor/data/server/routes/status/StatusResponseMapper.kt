package com.dd.callmonitor.data.server.routes.status

import com.dd.callmonitor.domain.callstatus.CallStatus
import com.dd.callmonitor.domain.contacts.TransformEmptyContactNameUseCase
import com.dd.callmonitor.domain.contacts.TransformEmptyPhoneNumberUseCase

internal class StatusResponseMapper(
    private val transformEmptyContactNameUseCase: TransformEmptyContactNameUseCase,
    private val transformEmptyPhoneNumberUseCase: TransformEmptyPhoneNumberUseCase
) {

    fun map(callStatus: CallStatus) = if (callStatus.ongoing) {
        StatusResponse(
            ongoing = true,
            name = transformEmptyContactNameUseCase(callStatus.name),
            number = transformEmptyPhoneNumberUseCase(callStatus.number)
        )
    } else {
        StatusResponse(
            ongoing = false,
            name = null,
            number = null
        )
    }
}
