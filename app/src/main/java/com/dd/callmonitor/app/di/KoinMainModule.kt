package com.dd.callmonitor.app.di

import android.app.Activity
import com.dd.callmonitor.domain.permissions.HandlePermissionUseCase
import com.dd.callmonitor.domain.permissions.RequestCallLogPermissionUseCase
import com.dd.callmonitor.domain.permissions.RequestPostNotificationsPermissionUseCase
import com.dd.callmonitor.presentation.main.MainPresenter
import com.dd.callmonitor.presentation.main.MainViewModel
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

    viewModel { (
                    activity: Activity,
                    requestCallLogPermissionUseCase: RequestCallLogPermissionUseCase,
                    requestPostNotificationsPermissionUseCase: RequestPostNotificationsPermissionUseCase
                ) ->
        val resources = androidContext().resources
        MainPresenter(
            handlePermissionUseCase = HandlePermissionUseCase(
                activity = activity,
                permissionsRepository = get(),
            ),
            observeWifiConnectivityUseCase = get(),
            requestCallLogPermissionUseCase = requestCallLogPermissionUseCase,
            requestPostNotificationsPermissionUseCase = requestPostNotificationsPermissionUseCase,
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
            viewModel = MainViewModel()
        )
    }
}
