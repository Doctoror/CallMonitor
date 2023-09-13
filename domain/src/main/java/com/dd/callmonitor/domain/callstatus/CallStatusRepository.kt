package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.Flow

interface CallStatusRepository {

    fun observeCallStatus(): Flow<Either<CallStatusError, CallStatus>>
}
