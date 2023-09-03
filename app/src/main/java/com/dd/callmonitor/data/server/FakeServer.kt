package com.dd.callmonitor.data.server

import kotlinx.coroutines.delay
import java.util.Random

private val random = Random()

class FakeServer(private val serverStateProvider: ServerStateProvider) : Server {

    private var running = false

    override suspend fun start() {
        serverStateProvider.state.emit(ServerState.Initialising)
        delay(1000L)
        if (random.nextBoolean()) {
            serverStateProvider.state.emit(ServerState.Running("192.168.1.2", "23231"))
            running = true
        } else {
            serverStateProvider.state.emit(ServerState.Error)
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
