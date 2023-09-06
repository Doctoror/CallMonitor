package com.dd.callmonitor.domain.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.AnyThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class ObserveWifiConnectivityUseCase(private val connectivityManager: ConnectivityManager) {

    private val lock = Any()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            // Note for code reviewers:
            // here we just terminate with exception if link properties cannot be derived.
            //
            // Additional use case can be created not to throw and handle the case where the ip
            // address cannot be derived and let user figure out their address manually.
            //
            // However, to save time I will just leave out this case and terminate early.
            val linkProperties = requireNotNull(connectivityManager.getLinkProperties(network)) {
                "No LinkProperties for network $network"
            }

            val siteLocalAddress = linkProperties
                .linkAddresses
                .map { it.address }
                .firstOrNull { it.isSiteLocalAddress }
            // Note for code reviewers. Same as comment for linkProperties/requireNotNull.
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
     * - `Optional.empty()` if the network state is unknown (expect a follow up)
     * - `Optional.of(false)` if not connected to a Wi-Fi network
     * - `Optional.of(true)`if connected to a Wi-Fi network
     */
    @AnyThread
    operator fun invoke(): Flow<ConnectivityState> =
        combine(connectivityStateFlow, connectivityStateFlow.subscriptionCount) { state, count ->
            // Note for the reviewers
            // This part is controversial, because we unnecessarily synchronize on lock and check
            // the callback flag on every emission of networkState. However, I can't think of a
            // better way to reliably encapsulate registering and unregistering a network callback
            // without making specific public methods for those, which would in turn make using this
            // use case not as seamless as it is now. As of now, no users of this use case have to
            // think about managing the connectivity manager as it would automatically unregister
            // once the last subscriber of subscriptionCount unsubscribes.
            if (count > 0) {
                registerCallbacksIfNotRegistered()
            } else {
                unregisterCallbacksIfRegistered()
            }

            state
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
