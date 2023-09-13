package com.dd.callmonitor.domain.connectivity

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.dd.callmonitor.domain.util.executeBlockAndCollectFromFlow
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowNetworkCapabilities
import java.net.InetAddress

@RunWith(RobolectricTestRunner::class)
class ObserveWifiConnectivityUseCaseTest {

    private val connectivityManager: ConnectivityManager = RuntimeEnvironment
        .getApplication()
        .getSystemService(ConnectivityManager::class.java)

    private val connectivityManagerShadow = Shadows.shadowOf(connectivityManager)

    private val isActiveNetworkWifiUseCase: IsActiveNetworkWifiUseCase = mockk()

    private val siteLocalAddress: InetAddress = mockk {
        every { isSiteLocalAddress } returns true
    }

    private val underTest = ObserveWifiConnectivityUseCase(
        connectivityManager,
        isActiveNetworkWifiUseCase
    )

    @Before
    fun before() {
        every { isActiveNetworkWifiUseCase() } returns false
    }

    @Test
    fun emitsDisconnectedAsFirstValueIfActiveNetworkIsNotWiFi() = runTest {
        assertEquals(ConnectivityState.Disconnected, underTest().first())
    }

    @Test
    fun skipsDisconnectedInitialStateIfActiveNetworkIsWiFi() {
        every { isActiveNetworkWifiUseCase() } returns true

        val collected = executeBlockAndCollectFromFlow(underTest()) {}

        assertEquals(emptyList<ConnectivityState>(), collected)
    }

    @Test
    fun emitsNetworkStateChangesFromNetworkCallback() {
        every { isActiveNetworkWifiUseCase() } returns true

        val network = setupWifiNetworkWithSiteLocalAddress()
        val collected = executeBlockAndCollectFromFlow(underTest()) {
            connectivityManagerShadow.networkCallbacks.forEach {
                it.onAvailable(network)
                it.onLost(network)
            }
        }

        assertEquals(
            listOf(
                ConnectivityState.Connected(siteLocalAddress),
                ConnectivityState.Disconnected
            ),
            collected
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun throwsWhenAvailableNetworkHasNoLinkProperties() {
        executeBlockAndCollectFromFlow(underTest()) {
            connectivityManagerShadow.networkCallbacks.forEach {
                it.onAvailable(setupWifiNetwork())
            }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun throwsWhenAvailableNetworkHasNoSiteLocalAddress() {
        executeBlockAndCollectFromFlow(underTest()) {
            connectivityManagerShadow.networkCallbacks.forEach {
                it.onAvailable(setupWifiNetworkWithEmptyLinkProperties())
            }
        }
    }

    private fun setupWifiNetwork(): Network =
        connectivityManager.activeNetwork!!.apply {
            connectivityManagerShadow.setNetworkCapabilities(
                this,
                Shadows
                    .shadowOf(ShadowNetworkCapabilities.newInstance())
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            )
        }

    private fun setupWifiNetworkWithEmptyLinkProperties(): Network =
        setupWifiNetwork().apply {
            connectivityManagerShadow.setLinkProperties(
                this,
                LinkProperties()
            )
        }

    private fun setupWifiNetworkWithSiteLocalAddress(): Network =
        setupWifiNetwork().apply {
            connectivityManagerShadow.setLinkProperties(
                this,
                LinkProperties().apply {
                    setLinkAddresses(listOf(mockk {
                        every { address } returns siteLocalAddress
                    }))
                }
            )
        }
}
