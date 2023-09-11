package com.dd.callmonitor.domain.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.AnyThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

class ObserveWifiConnectivityUseCase(
    private val connectivityManager: ConnectivityManager,
    private val isActiveNetworkWifiUseCase: IsActiveNetworkWifiUseCase
) {

    private val lock = Any()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            // Note for reviewers:
            // here we just terminate with exception if link properties cannot be derived.
            //
            // Additional use case can be created not to throw and handle the case where the ip
            // address cannot be derived and let user figure out their address manually.
            //
            // However, to reduce the scope of the project I will just leave out this feature and
            // terminate early.
            val linkProperties = requireNotNull(connectivityManager.getLinkProperties(network)) {
                "No LinkProperties for network $network"
            }

            val siteLocalAddress = linkProperties
                .linkAddresses
                .map { it.address }
                .firstOrNull { it.isSiteLocalAddress }
            // Note for reviewers: same as comment for linkProperties/requireNotNull.
                ?: throw IllegalStateException("Site-local address not found")


            connectivityStateFlow.value = ConnectivityState.Connected(siteLocalAddress)
        }

        override fun onLost(network: Network) {
            connectivityStateFlow.value = ConnectivityState.Disconnected
        }
    }

    private val connectivityStateFlow =
        MutableStateFlow<ConnectivityState>(ConnectivityState.Unknown)

    private var networkCallbackRegistered = false

    /**
     * @return a [Flow] that emits
     * - [ConnectivityState.Unknown] if the network state is unknown yet (a follow up guaranteed)
     * - [ConnectivityState.Connected] if connected to a Wi-Fi network
     * - [ConnectivityState.Disconnected] if not connected to a Wi-Fi network
     */
    @AnyThread
    operator fun invoke(): Flow<ConnectivityState> =
        combine(
            connectivityStateFlow.onStart(::connectivityStateFlowOnStart),
            // Note for the reviewers
            // This part is controversial, because we unnecessarily synchronize on lock and check
            // the callback flag on every emission of networkState. However, I can't think of a
            // better way to reliably encapsulate registering and unregistering a network callback
            // without making specific public methods for those, which would in turn make using this
            // use case not as seamless as it is now. As of now, no users of this use case have to
            // think about managing the connectivity manager as it would automatically unregister
            // once the last subscriber of connectivityStateFlow unsubscribes.
            connectivityStateFlow.subscriptionCount
        ) { state, count ->
            if (count > 0) {
                registerCallbacksIfNotRegistered()
            } else {
                unregisterCallbacksIfRegistered()
            }

            state
        }

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

    @AnyThread
    private fun registerCallbacksIfNotRegistered() {
        synchronized(lock) {
            if (!networkCallbackRegistered) {
                networkCallbackRegistered = true

                connectivityManager.registerNetworkCallback(
                    NetworkRequest
                        .Builder()
                        .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
                        .build(),
                    networkCallback
                )
            }
        }
    }

    @AnyThread
    private fun unregisterCallbacksIfRegistered() {
        synchronized(lock) {
            if (networkCallbackRegistered) {
                networkCallbackRegistered = false
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
    }
}
