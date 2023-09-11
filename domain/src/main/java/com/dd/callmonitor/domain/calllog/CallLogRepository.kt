package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either

interface CallLogRepository {

    suspend fun getCallLog(): Either<CallLogError, List<CallLogEntry>>
}
