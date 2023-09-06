package com.dd.callmonitor.domain.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.dd.callmonitor.domain.notifications.CheckPostNotificationsPermissionUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class CheckPostNotificationsPermissionUseCaseTest {

    private val context: Context = mockk()

    private val underTest = CheckPostNotificationsPermissionUseCase()

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun returnsPermissionGrantedPreTiramisu() {
        assertEquals(PackageManager.PERMISSION_GRANTED, underTest(context))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun doesNotCheckSelfPermissionPreTiramisu() {
        underTest(context)
        verify(exactly = 0) { context.checkSelfPermission(any()) }
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
    fun returnsPermissionStatusFromContextPostTiramisu() {
        val status = PackageManager.PERMISSION_DENIED
        every { context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) } returns status

        assertEquals(status, underTest(context))
    }
}
