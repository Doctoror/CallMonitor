package com.dd.callmonitor.data.server.routes.root

import kotlinx.serialization.Serializable

@Serializable
internal data class ServiceResponse(
    val name: String,
    val uri: String
)
