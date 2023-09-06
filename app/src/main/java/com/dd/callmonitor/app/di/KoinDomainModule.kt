package com.dd.callmonitor.app.di

import android.net.ConnectivityManager
import com.dd.callmonitor.app.components.server.StartServerUseCaseImpl
import com.dd.callmonitor.app.components.server.StopServerUseCaseImpl
import com.dd.callmonitor.data.calls.CallsRepositoryFactory
import com.dd.callmonitor.domain.calls.GetCallLogUseCase
import com.dd.callmonitor.domain.connectivity.FormatHostAndPortUseCase
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.notifications.CheckPostNotificationsPermissionUseCase
import com.dd.callmonitor.domain.server.StartServerUseCase
import com.dd.callmonitor.domain.server.StopServerUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinDomainModule() = module {

    factory { CallsRepositoryFactory().newInstance(androidContext().contentResolver) }

    factory { CheckPostNotificationsPermissionUseCase() }

    factory { FormatHostAndPortUseCase() }

    factory { GetCallLogUseCase(callsRepository = get()) }

    single {
        ObserveWifiConnectivityUseCase(
            connectivityManager = androidContext().getSystemService(ConnectivityManager::class.java)
        )
    }

    factory<StartServerUseCase> { StartServerUseCaseImpl(androidContext()) }

    factory<StopServerUseCase> { StopServerUseCaseImpl(androidContext()) }
}
