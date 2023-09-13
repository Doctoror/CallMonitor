package com.dd.callmonitor.data.server.routes.status

import com.dd.callmonitor.data.server.routes.RouteRegistrator
import com.dd.callmonitor.domain.callstatus.ObserveCallStatusUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.coroutines.flow.first

internal class StatusRouteRegistrator(
    private val observeCallStatusUseCase: ObserveCallStatusUseCase,
    private val statusResponseMapper: StatusResponseMapper
) : RouteRegistrator {

    override val serviceName = "status"

    override fun register(route: Route) {
        route.get("/$serviceName") {
            call.respond(
                observeCallStatusUseCase().first().fold(
                    onLeft = { it },
                    onRight = { statusResponseMapper.map(it) }
                )
            )
        }
    }
}

