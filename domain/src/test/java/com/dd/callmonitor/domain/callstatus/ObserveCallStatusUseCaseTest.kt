package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.Either
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveCallStatusUseCaseTest {

    private val callStatusRepository: CallStatusRepository = mockk()

    private val underTest = ObserveCallStatusUseCase(callStatusRepository)

    @Test
    fun emitsCallStatusFromRepository() = runTest {
        val value1: Either<CallStatusError, CallStatus> = mockk()
        val value2: Either<CallStatusError, CallStatus> = mockk()
        every { callStatusRepository.observeCallStatus() } returns flowOf(value1, value2)

        val output = underTest().toList()

        assertEquals(listOf(value1, value2), output)
    }
}
