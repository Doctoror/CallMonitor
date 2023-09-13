package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.Flow

interface CallLogRepository {

    fun observeCallLog(): Flow<Either<CallLogError, List<CallLogEntry>>>
}
