package com.dd.callmonitor.data.server.routes.root

import android.net.Uri
import com.dd.callmonitor.data.server.SCHEME
import com.dd.callmonitor.data.server.routes.RouteRegistrator
import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.domain.server.ServerStateProvider
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.coroutines.flow.first

internal class RootRouteRegistrator(
    private val responseTimeFormatter: ResponseTimeFormatter,
    private val serverStateProvider: ServerStateProvider,
    private val serviceNames: List<String>
) : RouteRegistrator {

    override val serviceName = ""

    override fun register(route: Route) {
        route.get("/") {
            val state = serverStateProvider.state.first()
            if (state is ServerState.Running) {
                call.respond(
                    RootResponse(
                        start = responseTimeFormatter.format(state.startedMillisUtc),
                        services = serviceNames.map { service ->
                            ServiceResponse(
                                name = service,
                                uri = Uri
                                    .Builder()
                                    .scheme(SCHEME)
                                    .encodedAuthority("${state.host}:${state.port}")
                                    .encodedPath(service)
                                    .build()
                                    .toString()
                            )
                        }
                    )
                )
            } else {
                // Note for reviewers: might better explain error, but this omitted to reduce the
                // scope of the project
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
