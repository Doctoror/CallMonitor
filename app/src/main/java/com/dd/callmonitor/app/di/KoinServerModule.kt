package com.dd.callmonitor.app.di

import com.dd.callmonitor.app.components.server.MakeServerStatusNotificationUseCase
import com.dd.callmonitor.data.server.ServerFactory
import com.dd.callmonitor.domain.notifications.ShowNotificationUseCase
import com.dd.callmonitor.domain.notifications.StartForegroundServiceUseCase
import com.dd.callmonitor.domain.server.ServerStateProvider
import com.dd.callmonitor.presentation.server.ServerPresenter
import com.dd.callmonitor.presentation.server.ServerViewModel
import com.dd.callmonitor.presentation.server.ForegroundServiceStatusMessageProvider
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun koinServerModule() = module {

    factory { MakeServerStatusNotificationUseCase() }

    factory {
        ForegroundServiceStatusMessageProvider(
            formatHostAndPortUseCase = get(),
            resources = get()
        )
    }

    single {
        ServerFactory().newInstance(
            getCallLogUseCase = get(),
            getCallStatusUseCase = get(),
            locale = get(),
            serverStateProvider = get()
        )
    }

    singleOf(::ServerStateProvider)

    factory { (scope: CoroutineScope) ->
        ServerPresenter(
            callStatusStartListeningUseCase = get(),
            callStatusStopListeningUseCase = get(),
            observeWifiConnectivityUseCase = get(),
            foregroundServiceStatusMessageProvider = get(),
            resources = get(),
            scope = scope,
            server = get(),
            serverStateProvider = get(),
            viewModel = ServerViewModel()
        )
    }

    factory { StartForegroundServiceUseCase() }

    factory {
        ShowNotificationUseCase(
            checkPermissionUseCase = get()
        )
    }
}
