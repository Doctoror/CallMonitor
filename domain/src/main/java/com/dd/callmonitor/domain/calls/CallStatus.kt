package com.dd.callmonitor.domain.calls

data class CallStatus(
    val ongoing: Boolean,
    val number: String,
    val name: String
)
