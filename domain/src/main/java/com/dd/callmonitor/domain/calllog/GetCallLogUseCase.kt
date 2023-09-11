package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either

/**
 * Note for reviewers: the use cases are needed to decouple "what to do" from "how to do". This
 * makes the business logic decoupled from repositories.
 */
class GetCallLogUseCase(private val callLogRepository: CallLogRepository) {

    suspend operator fun invoke(): Either<CallLogError, List<CallLogEntry>> =
        callLogRepository.getCallLog()
}
