package com.dd.callmonitor.domain.server

import java.net.InetAddress

interface Server {

    /**
     * Starts the server and binds to the specified host
     */
    suspend fun start(host: InetAddress)

    /**
     * Stops the server if running. If not running will do nothing.
     */
    suspend fun stopIfRunning()
}
