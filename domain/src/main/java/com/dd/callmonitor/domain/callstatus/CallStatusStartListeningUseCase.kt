package com.dd.callmonitor.domain.callstatus

class CallStatusStartListeningUseCase(private val callStatusRepository: CallStatusRepository) {

    operator fun invoke() = callStatusRepository.startListening()
}
