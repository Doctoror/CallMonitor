package com.dd.callmonitor.domain.callstatus

class CallStatusStopListeningUseCase(private val callStatusRepository: CallStatusRepository) {

    operator fun invoke() = callStatusRepository.stopListening()
}
