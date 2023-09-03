package com.dd.callmonitor.domain

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.AnyThread
import com.dd.callmonitor.framework.Optional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class ObserveWifiConnectivityUseCase(private val connectivityManager: ConnectivityManager) {

    private val lock = Any()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            networkState.value = Optional.of(true)
        }

        override fun onLost(network: Network) {
            networkState.value = Optional.of(false)
        }
    }

    private val networkState = MutableStateFlow<Optional<Boolean>>(Optional.empty())

    private var networkCallbackRegistered = false

    /**
     * @return a [Flow] that emits
     * - `Optional.empty()` if the network state is unknown (expect a follow up)
     * - `Optional.of(false)` if not connected to a Wi-Fi network
     * - `Optional.of(true)`if connected to a Wi-Fi network
     */
    @AnyThread
    operator fun invoke(): Flow<Optional<Boolean>> =
        combine(networkState, networkState.subscriptionCount) { state, count ->
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
