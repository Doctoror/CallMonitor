package com.dd.callmonitor.app.di

import org.junit.After
import org.koin.core.context.GlobalContext

interface KoinStoppingTest {

    @After
    fun after() {
        GlobalContext.stopKoin()
    }
}
