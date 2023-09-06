package com.dd.callmonitor.domain.calls

data class CallLogEntry(
    val beginningMillisUtc: Long,
    val durationSeconds: Long,
    val number: String,
    val name: String,
    val timesQueried: Int
)
