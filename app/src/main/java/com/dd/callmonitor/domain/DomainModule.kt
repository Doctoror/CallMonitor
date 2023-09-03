package com.dd.callmonitor.domain

import org.koin.dsl.module

fun koinDomainModule() = module {

    factory { FormatIpAndPortUseCase() }
}
