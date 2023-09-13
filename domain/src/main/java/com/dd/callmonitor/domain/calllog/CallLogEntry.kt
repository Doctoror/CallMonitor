package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Optional

data class CallLogEntry(
    val beginningMillisUtc: Long,
    val durationSeconds: Long,
    val number: Optional<String>,
    val name: Optional<String>,
    val timesQueried: Int
)
