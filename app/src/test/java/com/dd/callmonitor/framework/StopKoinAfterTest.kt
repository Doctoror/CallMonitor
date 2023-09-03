package com.dd.callmonitor.framework

import org.junit.After
import org.koin.core.context.GlobalContext

open class StopKoinAfterTest {

    @After
    fun after() {
        GlobalContext.stopKoin()
    }
}
