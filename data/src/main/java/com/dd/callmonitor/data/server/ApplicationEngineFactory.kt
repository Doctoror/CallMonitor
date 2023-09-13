package com.dd.callmonitor.data.server

import com.dd.callmonitor.data.server.routes.RouteRegistrator
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

internal class ApplicationEngineFactory(
    private val routeRegistrators: Iterable<RouteRegistrator>
) {

    @OptIn(ExperimentalSerializationApi::class)
    fun newInstance(host: String, port: Int): ApplicationEngine =
        embeddedServer(Netty, host = host, port = port) {

            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                    prettyPrint = true
                })
            }

            routing {
                routeRegistrators.forEach { it.register(this@routing) }
            }
        }
}
