package com.dd.callmonitor.domain.connectivity

class FormatHostAndPortUseCase {

    operator fun invoke(host: String, port: Int) = "$host:$port"
}
