package com.dd.callmonitor.domain.connectivity

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class IsActiveNetworkWifiUseCase(
    private val connectivityManager: ConnectivityManager
) {

    /**
     * @return true if [ConnectivityManager.getActiveNetwork] is Wi-Fi.
     */
    operator fun invoke(): Boolean {
        connectivityManager.activeNetwork?.run {
            connectivityManager.getNetworkCapabilities(this)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                }
            }
        }
        return false
    }
}
