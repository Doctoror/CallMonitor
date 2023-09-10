package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.ResultOrFailure

interface CallLogRepository {

    suspend fun getCallLog(): ResultOrFailure<List<CallLogEntry>, CallLogError>
}
