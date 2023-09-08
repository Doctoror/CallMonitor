package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.ResultOrFailure
import kotlinx.coroutines.flow.first

class GetCallStatusUseCase(private val callStatusRepository: CallStatusRepository) {

    suspend operator fun invoke(): ResultOrFailure<CallStatus, CallStatusError> =
        callStatusRepository.observeCallStatus().first()
}
