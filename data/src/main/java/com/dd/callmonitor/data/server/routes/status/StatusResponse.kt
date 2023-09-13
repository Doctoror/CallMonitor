package com.dd.callmonitor.data.server.routes.status

import kotlinx.serialization.Serializable

@Serializable
internal data class StatusResponse(
    val ongoing: Boolean,
    val number: String?,
    val name: String?
)
