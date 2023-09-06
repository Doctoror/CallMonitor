package com.dd.callmonitor.domain.server

import java.net.InetAddress

interface Server {

    suspend fun start(host: InetAddress)

    suspend fun stopIfRunning()
}
