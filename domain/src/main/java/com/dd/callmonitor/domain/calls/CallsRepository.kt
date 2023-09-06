package com.dd.callmonitor.domain.calls

interface CallsRepository {

    fun getCallLog(): List<CallLogEntry>

    fun getStatus(): CallStatus
}
