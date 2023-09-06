package com.dd.callmonitor.domain.connectivity

class FormatHostAndPortUseCase {

    operator fun invoke(post: String, port: Int) = "$post:$port"
}
