package com.dd.callmonitor.data.server.routes.status

import com.dd.callmonitor.data.server.routes.RouteRegistrator
import com.dd.callmonitor.domain.callstatus.GetCallStatusUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal class StatusRouteRegistrator(
    private val getCallStatusUseCase: GetCallStatusUseCase,
    private val statusResponseMapper: StatusResponseMapper
) : RouteRegistrator {

    override val serviceName = "status"

    override fun register(route: Route) {
        route.get("/$serviceName") {
            call.respond(
                getCallStatusUseCase().fold(
                    onLeft = { it },
                    onRight = { statusResponseMapper.map(it) }
                )
            )
        }
    }
}

