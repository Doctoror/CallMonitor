package com.dd.callmonitor.data.server.routes

import io.ktor.server.routing.Route

interface RouteRegistrator {

    fun register(route: Route)
}
