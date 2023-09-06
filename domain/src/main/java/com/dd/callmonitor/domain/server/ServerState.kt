package com.dd.callmonitor.domain.server

sealed class ServerState {

    data class Error(val error: ServerError) : ServerState()
    data object Idle : ServerState()
    data object Initialising : ServerState()
    data class Running(val host: String, val port: Int, val startedMillisUtc: Long) : ServerState()
    data object Stopping : ServerState()
}
