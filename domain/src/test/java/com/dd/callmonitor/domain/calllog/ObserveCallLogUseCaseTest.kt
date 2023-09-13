package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveCallLogUseCaseTest {

    private val callLogRepository: CallLogRepository = mockk()

    private val underTest = ObserveCallLogUseCase(callLogRepository)

    @Test
    fun emitsCallLogFromRepository() = runTest {
        val value1: Either<CallLogError, List<CallLogEntry>> = mockk()
        val value2: Either<CallLogError, List<CallLogEntry>> = mockk()
        every { callLogRepository.observeCallLog() } returns flowOf(value1, value2)

        val output = underTest().toList()

        assertEquals(listOf(value1, value2), output)
    }
}
