package com.dd.callmonitor.domain.connectivity

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatHostAndPortUseCaseTest {

    private val underTest = FormatHostAndPortUseCase()

    @Test
    fun formatsWithBlankHost() {
        assertEquals(":0", underTest("", 0))
    }

    @Test
    fun formatsHostAndPort() {
        assertEquals("host:36001", underTest("host", 36001))
    }
}
