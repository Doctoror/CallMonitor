package com.dd.callmonitor.domain.callstatus

import com.dd.callmonitor.domain.util.Optional

data class CallStatus(
    val ongoing: Boolean,
    val number: Optional<String>,
    val name: Optional<String>
)
