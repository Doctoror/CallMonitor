package com.dd.callmonitor.domain.phonenumbers

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class NormalizePhoneNumberUseCaseTest {

    private val underTest = NormalizePhoneNumberUseCase(Locale.UK)

    @Test
    fun returnsBlankForBlankInput() {
        assertEquals("", underTest(""))
    }

    @Test
    fun normalizesNumberWithDashesToE164() {
        assertEquals("+442033739672", underTest("203-3739672"))
    }

    @Test
    fun normalizesNumberWithSpacesToE164() {
        assertEquals("+442072895728", underTest("207 2895728"))
    }
}
