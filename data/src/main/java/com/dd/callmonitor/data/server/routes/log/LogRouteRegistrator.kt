package com.dd.callmonitor.data.server.routes.log

import com.dd.callmonitor.data.server.routes.RouteRegistrator
import com.dd.callmonitor.domain.calls.GetCallLogUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal class LogRouteRegistrator(
    private val getCallLogUseCase: GetCallLogUseCase,
    private val callLogResponseMapper: CallLogResponseMapper
) : RouteRegistrator {

    override fun register(route: Route) {
        route.get("/log") {
            call.respond(
                getCallLogUseCase().map(callLogResponseMapper::map)
            )
        }
    }
}
