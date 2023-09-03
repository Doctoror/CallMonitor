package com.dd.callmonitor.framework.lifecycle

import androidx.lifecycle.Lifecycle
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class LifecycleViewModelPresenterTest {

    @Test
    fun dispatchesOnCreate() {
        val underTest = TestPresenter()

        underTest.onStateChanged(mockk(), Lifecycle.Event.ON_CREATE)

        assertTrue(underTest.created)
    }

    private class TestPresenter : LifecycleViewModelPresenter() {

        var created = false

        override fun onCreate() {
            created = true
        }
    }
}
