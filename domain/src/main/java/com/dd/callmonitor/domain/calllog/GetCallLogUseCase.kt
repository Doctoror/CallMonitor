package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either

class GetCallLogUseCase(private val callLogRepository: CallLogRepository) {

    suspend operator fun invoke(): Either<CallLogError, List<CallLogEntry>> =
        callLogRepository.getCallLog()
}
