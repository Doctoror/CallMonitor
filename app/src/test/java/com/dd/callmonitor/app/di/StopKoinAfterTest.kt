package com.dd.callmonitor.app.di

import org.junit.After
import org.koin.core.context.GlobalContext

open class StopKoinAfterTest {

    @After
    fun after() {
        GlobalContext.stopKoin()
    }
}
