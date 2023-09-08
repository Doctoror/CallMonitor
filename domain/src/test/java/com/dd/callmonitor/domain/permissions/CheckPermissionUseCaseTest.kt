package com.dd.callmonitor.domain.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class CheckPermissionUseCaseTest {

    private val context: Context = mockk()

    private val underTest = CheckPermissionUseCase(context)

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun invokesWhenGrantedWhenPlatformLessThanPermission() {
        testGrantedOrDenied(
            permission = ApiLevelPermissions.POST_NOTIFICATIONS,
            expectGranted = true
        )
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S])
    fun doesNotCheckSelfPermissionWhenPlatformLessThanPermission() {
        underTest(ApiLevelPermissions.POST_NOTIFICATIONS,
            whenDenied = {},
            whenGranted = {}
        )
        verify(exactly = 0) { context.checkSelfPermission(any()) }
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
    fun invokesWhenGrantedIfGrantedFromContext() {
        val status = PackageManager.PERMISSION_GRANTED
        val permission = ApiLevelPermissions.POST_NOTIFICATIONS
        every { context.checkSelfPermission(permission.permission) } returns status

        testGrantedOrDenied(
            permission = permission,
            expectGranted = true
        )
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU])
    fun invokesWhenDeniedIfDeniedFromContext() {
        val status = PackageManager.PERMISSION_DENIED
        val permission = ApiLevelPermissions.POST_NOTIFICATIONS
        every { context.checkSelfPermission(permission.permission) } returns status

        testGrantedOrDenied(
            permission = permission,
            expectGranted = false
        )
    }

    private fun testGrantedOrDenied(permission: ApiLevelPermission, expectGranted: Boolean) {
        var whenDeniedInvoked = false
        var whenGrantedInvoked = false

        underTest.invoke(
            permission = permission,
            whenGranted = { whenGrantedInvoked = true },
            whenDenied = { whenDeniedInvoked = true }
        )

        if (expectGranted) {
            assertTrue(whenGrantedInvoked)
            assertFalse(whenDeniedInvoked)
        } else {
            assertTrue(whenDeniedInvoked)
            assertFalse(whenGrantedInvoked)
        }
    }
}
