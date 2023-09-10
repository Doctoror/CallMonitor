package com.dd.callmonitor.data.server.routes

import io.ktor.server.routing.Route

internal interface RouteRegistrator {

    val serviceName: String

    fun register(route: Route)
}
