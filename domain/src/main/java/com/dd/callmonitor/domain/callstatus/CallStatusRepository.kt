package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.StateFlow

interface CallStatusRepository {

    fun observeCallStatus(): StateFlow<Either<CallStatusError, CallStatus>>

    fun startListening()

    fun stopListening()
}
