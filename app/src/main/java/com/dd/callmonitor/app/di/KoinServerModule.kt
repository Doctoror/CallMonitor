package com.dd.callmonitor.app.di

import com.dd.callmonitor.app.components.server.ServerStatusNotificationProvider
import com.dd.callmonitor.data.server.ServerFactory
import com.dd.callmonitor.domain.server.ServerStateProvider
import com.dd.callmonitor.presentation.server.ForegroundServiceStatusMessageProvider
import com.dd.callmonitor.presentation.server.ServerErrorNotificationMessageProvider
import com.dd.callmonitor.presentation.server.ServerPresenter
import com.dd.callmonitor.presentation.server.ServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun koinServerModule() = module {

    factory { ServerStatusNotificationProvider() }

    factory {
        ForegroundServiceStatusMessageProvider(
            formatHostAndPortUseCase = get(),
            resources = get()
        )
    }

    single {
        ServerFactory().newInstance(
            dispatcherIo = get<Dispatchers>().IO,
            locale = get(),
            observeCallLogUseCase = get(),
            observeCallStatusUseCase = get(),
            serverStateProvider = get()
        )
    }

    singleOf(::ServerStateProvider)

    factory { (scope: CoroutineScope) ->
        ServerPresenter(
            observeWifiConnectivityUseCase = get(),
            foregroundServiceStatusMessageProvider = get(),
            scope = scope,
            server = get(),
            serverErrorNotificationMessageProvider = ServerErrorNotificationMessageProvider(
                resources = get()
            ),
            serverStateProvider = get(),
            viewModel = ServerViewModel()
        )
    }
}
