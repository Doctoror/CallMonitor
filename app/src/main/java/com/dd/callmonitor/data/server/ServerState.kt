package com.dd.callmonitor.data.server

sealed class ServerState {

    data object Error : ServerState()
    data object Idle : ServerState()
    data object Initialising : ServerState()
    data class Running(val ip: String, val port: String) : ServerState()
    data object Stopping : ServerState()
}
