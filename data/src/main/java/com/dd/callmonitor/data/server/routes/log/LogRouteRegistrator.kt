package com.dd.callmonitor.data.server.routes.log

import com.dd.callmonitor.data.server.routes.RouteRegistrator
import com.dd.callmonitor.domain.calllog.ObserveCallLogUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.coroutines.flow.first

internal class LogRouteRegistrator(
    private val observeCallLogUseCase: ObserveCallLogUseCase,
    private val callLogResponseMapper: CallLogResponseMapper
) : RouteRegistrator {

    override val serviceName = "log"

    override fun register(route: Route) {
        route.get("/$serviceName") {
            call.respond(
                observeCallLogUseCase().first().fold(
                    onLeft = { it },
                    onRight = { it.map(callLogResponseMapper::map) }
                )
            )
        }
    }
}
