package com.dd.callmonitor.data.server.routes.log

import com.dd.callmonitor.data.server.routes.RouteRegistrator
import com.dd.callmonitor.domain.calllog.GetCallLogUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal class LogRouteRegistrator(
    private val getCallLogUseCase: GetCallLogUseCase,
    private val callLogResponseMapper: CallLogResponseMapper
) : RouteRegistrator {

    override val serviceName = "log"

    override fun register(route: Route) {
        route.get("/$serviceName") {
            call.respond(
                getCallLogUseCase().fold(
                    onLeft = { it },
                    onRight = { it.map(callLogResponseMapper::map) }
                )
            )
        }
    }
}
