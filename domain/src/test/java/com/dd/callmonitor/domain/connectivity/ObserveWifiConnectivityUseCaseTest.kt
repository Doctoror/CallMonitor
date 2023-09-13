package com.dd.callmonitor.domain.connectivity

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun skipsDisconnectedInitialStateIfActiveNetworkIsWiFi() {
        every { isActiveNetworkWifiUseCase() } returns true

        val collected = mutableListOf<ConnectivityState>()
        runTest(UnconfinedTestDispatcher()) {
            val collectJob = launch {
                underTest().collect(collected::add)
            }

            collectJob.cancel()
        }

        assertEquals(emptyList<ConnectivityState>(), collected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitsNetworkStateChangesFromNetworkCallback() {
        every { isActiveNetworkWifiUseCase() } returns true

        val network = setupWifiNetworkWithSiteLocalAddress()
        val collected = mutableListOf<ConnectivityState>()

        runTest(UnconfinedTestDispatcher()) {
            val collectJob = launch {
                underTest().collect(collected::add)
            }

            connectivityManagerShadow.networkCallbacks.forEach {
                it.onAvailable(network)
                it.onLost(network)
            }

            collectJob.cancel()
        }

        assertEquals(
            listOf(
                ConnectivityState.Connected(siteLocalAddress),
                ConnectivityState.Disconnected
            ),
            collected
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = IllegalArgumentException::class)
    fun throwsWhenAvailableNetworkHasNoLinkProperties() {
        runTest(UnconfinedTestDispatcher()) {
            val collectJob = launch { underTest().collect {} }

            connectivityManagerShadow.networkCallbacks.forEach {
                it.onAvailable(setupWifiNetwork())
            }

            collectJob.cancel()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = IllegalStateException::class)
    fun throwsWhenAvailableNetworkHasNoSiteLocalAddress() {
        runTest(UnconfinedTestDispatcher()) {
            val collectJob = launch { underTest().collect {} }

            connectivityManagerShadow.networkCallbacks.forEach {
                it.onAvailable(setupWifiNetworkWithEmptyLinkProperties())
            }

            collectJob.cancel()
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
