package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.Flow

/**
 * Note for reviewers: the use cases are needed to decouple "what to do" from "how to do". This
 * makes the business logic decoupled from repositories.
 *
 * For example, refactoring a repository interface would not leak out to a `presentation` layer, and
 * a single change in a repository would need to be changed in a single use case, but if the
 * repository would have been used many times in other layers a change in the repository would
 * render lots of changes across the app.
 */
class ObserveCallLogUseCase(private val callLogRepository: CallLogRepository) {

    operator fun invoke(): Flow<Either<CallLogError, List<CallLogEntry>>> =
        callLogRepository.observeCallLog()
}
