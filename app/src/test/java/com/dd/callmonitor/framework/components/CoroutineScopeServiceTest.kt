package com.dd.callmonitor.framework.components

import android.content.Intent
import com.dd.callmonitor.framework.StopKoinAfterTest
import kotlinx.coroutines.isActive
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class CoroutineScopeServiceTest : StopKoinAfterTest() {

    @Test
    fun scopeIsActiveByDefault() {
        assertTrue(TestService().exposeScope().isActive)
    }

    @Test
    fun scopeIsNotActiveOnDestroy() {
        val underTest = TestService()

        underTest.onDestroy()

        assertFalse(underTest.exposeScope().isActive)
    }

    private class TestService : CoroutineScopeService() {

        fun exposeScope() = scope

        override fun onBind(p0: Intent?) = null
    }
}
