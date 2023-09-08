package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.ResultOrFailure
import kotlinx.coroutines.flow.StateFlow

interface CallStatusRepository {

    fun observeCallStatus(): StateFlow<ResultOrFailure<CallStatus, CallStatusError>>

    fun startListening()

    fun stopListening()
}
