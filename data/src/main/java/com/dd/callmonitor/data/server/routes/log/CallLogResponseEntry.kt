package com.dd.callmonitor.data.server.routes.log

import kotlinx.serialization.Serializable

@Serializable
internal data class CallLogResponseEntry(
    val beginning: String,
    val duration: String,
    val number: String?,
    val name: String?,
    val timesQueried: Int
)
