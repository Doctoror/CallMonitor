package com.dd.callmonitor.app.di

import com.dd.callmonitor.presentation.calllog.CallLogEntryViewModelMapper
import com.dd.callmonitor.presentation.calllog.CallLogPresenter
import com.dd.callmonitor.presentation.calllog.CallLogViewModel
import com.dd.callmonitor.presentation.calllog.CallLogViewModelUpdater
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsPresenter
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsViewModel
import com.dd.callmonitor.presentation.main.usecases.ServerErrorViewModelUpdater
import com.dd.callmonitor.presentation.main.usecases.ServerIdleViewModelUpdater
import com.dd.callmonitor.presentation.main.usecases.ServerInitializingViewModelUpdater
import com.dd.callmonitor.presentation.main.usecases.ServerRunningViewModelUpdater
import com.dd.callmonitor.presentation.main.usecases.ServerStateViewModelUpdater
import com.dd.callmonitor.presentation.main.usecases.ServerStoppingViewModelUpdater
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun koinMainModule() = module {

    viewModel {
        CallLogPresenter(
            callLogViewModelUpdater = CallLogViewModelUpdater(
                callLogEntryViewModelMapper = CallLogEntryViewModelMapper(
                    locale = get(),
                    resources = get()
                )
            ),
            getCallLogUseCase = get(),
            viewModel = CallLogViewModel()
        )
    }

    viewModel {
        val resources = androidContext().resources
        ServerControlsPresenter(
            observeWifiConnectivityUseCase = get(),
            serverStateProvider = get(),
            startServerUseCase = get(),
            stopServerUseCase = get(),
            // Note for reviewers:
            // You might argue that constructing these dependencies needs to be moved to a separate
            // factory in Koin. However, if this is used only in this presenter constructor, it
            // doesn't make sense to add knowledge about how to construct these dependencies to Koin
            serverStateViewModelUpdater = ServerStateViewModelUpdater(
                ServerErrorViewModelUpdater(resources),
                ServerIdleViewModelUpdater(resources),
                ServerInitializingViewModelUpdater(resources),
                ServerRunningViewModelUpdater(get(), resources),
                ServerStoppingViewModelUpdater(resources)
            ),
            viewModel = ServerControlsViewModel()
        )
    }
}
