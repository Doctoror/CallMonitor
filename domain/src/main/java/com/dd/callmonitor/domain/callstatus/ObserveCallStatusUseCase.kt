package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.Flow

class ObserveCallStatusUseCase(private val callStatusRepository: CallStatusRepository) {

    operator fun invoke(): Flow<Either<CallStatusError, CallStatus>> =
        callStatusRepository.observeCallStatus()
}
