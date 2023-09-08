package com.dd.callmonitor.data.server.routes.status

import com.dd.callmonitor.domain.callstatus.CallStatus

internal class StatusResponseMapper {

    fun map(callStatus: CallStatus) = StatusResponse(
        ongoing = callStatus.ongoing,
        name = callStatus.name,
        number = callStatus.number
    )
}
