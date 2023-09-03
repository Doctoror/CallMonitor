package com.dd.callmonitor.data.server

interface Server {

    suspend fun start()

    suspend fun stopIfRunning()
}
