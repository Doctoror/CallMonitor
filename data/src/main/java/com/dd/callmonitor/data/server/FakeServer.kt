package com.dd.callmonitor.data.server

import com.dd.callmonitor.domain.server.Server
import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.domain.server.ServerStateProvider
import kotlinx.coroutines.delay
import java.net.InetAddress
import java.util.Random

private val random = Random()

internal class FakeServer(private val serverStateProvider: ServerStateProvider) : Server {

    private var running = false

    override suspend fun start(host: InetAddress) {
        serverStateProvider.state.emit(ServerState.Initialising)
        delay(1000L)
        if (random.nextBoolean()) {
            serverStateProvider.state.emit(
                ServerState.Running(
                    host.hostAddress!!,
                    PORT,
                    System.currentTimeMillis()
                )
            )
            running = true
        } else {
            serverStateProvider.state.emit(ServerState.Error(ServerError.GENERIC))
        }
    }

    override suspend fun stopIfRunning() {
        if (running) {
            running = false
            serverStateProvider.state.emit(ServerState.Stopping)
            delay(1000L)
            serverStateProvider.state.emit(ServerState.Idle)
        }
    }
}
