package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.Flow

/**
 * Note for reviewers: the use cases are needed to decouple "what to do" from "how to do". This
 * makes the business logic decoupled from repositories.
 */
class ObserveCallLogUseCase(private val callLogRepository: CallLogRepository) {

    operator fun invoke(): Flow<Either<CallLogError, List<CallLogEntry>>> =
        callLogRepository.observeCallLog()
}
