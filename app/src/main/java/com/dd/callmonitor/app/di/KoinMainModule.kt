package com.dd.callmonitor.app.di

import com.dd.callmonitor.presentation.calllog.CallLogEntryViewModelMapper
import com.dd.callmonitor.presentation.calllog.CallLogPresenter
import com.dd.callmonitor.presentation.calllog.CallLogViewModel
import com.dd.callmonitor.presentation.calllog.CallLogViewModelUpdater
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsPresenter
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsViewModel
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerErrorUseCase
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerIdleUseCase
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerInitializingUseCase
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerRunningUseCase
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerStateUseCase
import com.dd.callmonitor.presentation.main.usecases.UpdateViewModelOnServerStoppingUseCase
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
            updateViewModelOnServerStateUseCase = UpdateViewModelOnServerStateUseCase(
                UpdateViewModelOnServerErrorUseCase(resources),
                UpdateViewModelOnServerIdleUseCase(resources),
                UpdateViewModelOnServerInitializingUseCase(resources),
                UpdateViewModelOnServerRunningUseCase(get(), resources),
                UpdateViewModelOnServerStoppingUseCase(resources)
            ),
            viewModel = ServerControlsViewModel()
        )
    }
}
