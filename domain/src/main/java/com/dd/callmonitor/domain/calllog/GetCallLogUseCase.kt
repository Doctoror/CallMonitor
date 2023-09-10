package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.ResultOrFailure

class GetCallLogUseCase(private val callLogRepository: CallLogRepository) {

    suspend operator fun invoke(): ResultOrFailure<List<CallLogEntry>, CallLogError> =
        callLogRepository.getCallLog()
}
