package com.dd.callmonitor.data.server.routes.root

import kotlinx.serialization.Serializable

@Serializable
internal data class RootResponse(
    val start: String,
    val services: List<ServiceResponse>
)
