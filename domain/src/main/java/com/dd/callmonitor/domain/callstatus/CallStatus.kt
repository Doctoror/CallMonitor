package com.dd.callmonitor.domain.callstatus

data class CallStatus(
    val ongoing: Boolean,
    val number: String,
    val name: String
)
