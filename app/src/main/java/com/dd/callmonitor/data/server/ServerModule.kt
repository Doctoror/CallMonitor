package com.dd.callmonitor.data.server

import com.dd.callmonitor.domain.server.StartForegroundServiceUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun koinServerModule() = module {

    singleOf(::ServerStateProvider)

    single<Server> { FakeServer(get()) }

    factory { StartForegroundServiceUseCase() }
}
