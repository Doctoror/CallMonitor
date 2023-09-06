package com.dd.callmonitor.data.server.routes.status

import com.dd.callmonitor.domain.calls.CallsRepository
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal fun Route.status(
    callsRepository: CallsRepository,
    statusResponseMapper: StatusResponseMapper
) {
    get("/status") {
        call.respond(
            callsRepository
                .getStatus()
                .let(statusResponseMapper::map)
        )
    }
}
