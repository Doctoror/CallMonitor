package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.first

class GetCallStatusUseCase(private val callStatusRepository: CallStatusRepository) {

    suspend operator fun invoke(): Either<CallStatusError, CallStatus> =
        callStatusRepository.observeCallStatus().first()
}
