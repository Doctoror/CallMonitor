package com.dd.callmonitor.domain.connectivity

import java.net.InetAddress

sealed class ConnectivityState {

    data object Disconnected : ConnectivityState()
    data class Connected(val siteLocalAddress: InetAddress) : ConnectivityState()
}
