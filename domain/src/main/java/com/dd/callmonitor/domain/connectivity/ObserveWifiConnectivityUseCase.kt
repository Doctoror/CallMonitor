package com.dd.callmonitor.domain.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import java.net.InetAddress

class ObserveWifiConnectivityUseCase(
    private val connectivityManager: ConnectivityManager,
    private val isActiveNetworkWifiUseCase: IsActiveNetworkWifiUseCase
) {

    /**
     * @return a [Flow] that emits
     * - [ConnectivityState.Unknown] if the network state is unknown yet (a follow up guaranteed)
     * - [ConnectivityState.Connected] if connected to a Wi-Fi network
     * - [ConnectivityState.Disconnected] if not connected to a Wi-Fi network
     */
    operator fun invoke(): Flow<ConnectivityState> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                trySend(ConnectivityState.Connected(network.getSiteLocalAddressOrThrow()))
            }

            override fun onLost(network: Network) {
                trySend(ConnectivityState.Disconnected)
            }
        }

        connectivityManager.registerNetworkCallback(
            NetworkRequest
                .Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build(),
            networkCallback
        )

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.onStart(::connectivityStateFlowOnStart)

    /**
     * [ConnectivityManager.NetworkCallback] does not emit default value when requested network is
     * not available, see
     * [onLost in NetworkCallback doesn't work when I launch the app](https://stackoverflow.com/q/70324348)
     *
     * So there is no way to know if Wi-Fi is connected until we receive NetworkCallback, which we
     * won't if it's not, so, we must assume that it's not connected by default.
     *
     * But we don't want to emit [ConnectivityState.Disconnected] as a default value if it will be
     * followed by [ConnectivityManager.NetworkCallback.onAvailable], because observing UI will show
     * a "Disconnected" state for a brief moment, which we don't want to see.
     *
     * We cannot [ConnectivityManager.getAllNetworks] to search for an initial Wi-Fi network state,
     * because it's deprecated.
     *
     * So we can at least check for [ConnectivityManager.getActiveNetwork] and if it's Wi-Fi, we can
     * at least avoid emitting [ConnectivityState.Disconnected] by default.
     */
    private suspend fun connectivityStateFlowOnStart(collector: FlowCollector<ConnectivityState>) {
        if (!isActiveNetworkWifiUseCase()) {
            collector.emit(ConnectivityState.Disconnected)
        }
    }

    private fun Network.getSiteLocalAddressOrThrow(): InetAddress {
        // Note for reviewers:
        // here we just terminate with exception if link properties cannot be derived.
        //
        // Additional use case can be created not to throw and handle the case where the ip
        // address cannot be derived and let user figure out their address manually.
        //
        // However, to reduce the scope of the project I will just leave out this feature and
        // terminate early.
        val linkProperties = requireNotNull(connectivityManager.getLinkProperties(this)) {
            "No LinkProperties for network $this"
        }

        return linkProperties
            .linkAddresses
            .map { it.address }
            .firstOrNull { it.isSiteLocalAddress }
        // Note for reviewers: same as comment for linkProperties/requireNotNull.
            ?: throw IllegalStateException("Site-local address not found")
    }
}
