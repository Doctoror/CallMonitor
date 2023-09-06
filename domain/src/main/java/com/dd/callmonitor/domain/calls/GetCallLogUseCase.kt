package com.dd.callmonitor.domain.calls

class GetCallLogUseCase(private val callsRepository: CallsRepository) {

    operator fun invoke(): List<CallLogEntry> = callsRepository.getCallLog()
}
