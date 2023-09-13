package com.dd.callmonitor.domain.connectivity

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowNetworkCapabilities

@RunWith(RobolectricTestRunner::class)
class IsActiveNetworkWifiUseCaseTest {

    private val connectivityManager: ConnectivityManager = RuntimeEnvironment
        .getApplication()
        .getSystemService(ConnectivityManager::class.java)

    private val underTest = IsActiveNetworkWifiUseCase(connectivityManager)

    @Test
    fun returnsFalseWhenActiveNetworkIsNotActive() {
        Shadows
            .shadowOf(connectivityManager)
            .setDefaultNetworkActive(false)

        assertFalse(underTest())
    }

    @Test
    fun returnsFalseWhenActiveNetworkHasNoCapabilities() {
        Shadows
            .shadowOf(connectivityManager)
            .setNetworkCapabilities(connectivityManager.activeNetwork, null)

        assertFalse(underTest())
    }

    @Test
    fun returnsFalseWhenActiveNetworkIsNotWiFi() {
        Shadows
            .shadowOf(connectivityManager)
            .setNetworkCapabilities(
                connectivityManager.activeNetwork,
                Shadows
                    .shadowOf(ShadowNetworkCapabilities.newInstance())
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            )

        assertFalse(underTest())
    }

    @Test
    fun returnsTrueWhenActiveNetworkIsWiFi() {
        Shadows
            .shadowOf(connectivityManager)
            .setNetworkCapabilities(
                connectivityManager.activeNetwork,
                Shadows
                    .shadowOf(ShadowNetworkCapabilities.newInstance())
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            )

        assertTrue(underTest())
    }
}
