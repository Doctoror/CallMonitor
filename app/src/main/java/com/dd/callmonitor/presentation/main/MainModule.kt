package com.dd.callmonitor.presentation.main

import com.dd.callmonitor.domain.notifications.RequestNotificationsPermissionUseCase
import com.dd.callmonitor.presentation.main.usecases.StartServerUseCase
import com.dd.callmonitor.presentation.main.usecases.StopServerUseCase
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
        val resources = androidContext().resources
        MainPresenter(
            checkPostNotificationsPermissionUseCase = get(),
            notificationPreferencesRepository = get(),
            serverStateProvider = get(),
            // Note for reviewers:
            // You might argue that constructing these dependencies needs to be moved to a separate
            // factory in Koin. However, if this is used only in this presenter constructor, it
            // doesn't make sense to add knowledge about how to construct these dependencies to Koin
            requestNotificationsPermissionUseCase = RequestNotificationsPermissionUseCase(
                activityResultLauncher = it.get()
            ),
            startServerUseCase = StartServerUseCase(androidContext()),
            stopServerUseCase = StopServerUseCase(androidContext()),
            updateViewModelOnServerStateUseCase = UpdateViewModelOnServerStateUseCase(
                UpdateViewModelOnServerErrorUseCase(resources),
                UpdateViewModelOnServerIdleUseCase(resources),
                UpdateViewModelOnServerInitializingUseCase(resources),
                UpdateViewModelOnServerRunningUseCase(resources),
                UpdateViewModelOnServerStoppingUseCase(resources)
            ),
            viewModel = MainViewModel()
        )
    }
}
