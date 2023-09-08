package com.dd.callmonitor.app.di

import android.app.Activity
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.check.checkModules
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KoinModulesTest : StopKoinAfterTest() {

    @Test
    fun definitionsCanRun() {
        GlobalContext.get().checkModules {
            withInstance<Activity>(mockk())
            withInstance<CoroutineScope>(mockk())
        }
    }
}
