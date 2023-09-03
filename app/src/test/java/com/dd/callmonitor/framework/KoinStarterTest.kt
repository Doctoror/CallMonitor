package com.dd.callmonitor.framework

import androidx.activity.result.ActivityResultLauncher
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.test.check.checkModules
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KoinStarterTest : StopKoinAfterTest() {

    @Test
    fun definitionsCanRun() {
        GlobalContext.get().checkModules {
            withInstance<ActivityResultLauncher<String>>(mockk())
        }
    }
}
