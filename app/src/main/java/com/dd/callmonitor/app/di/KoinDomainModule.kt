package com.dd.callmonitor.app.di

import android.net.ConnectivityManager
import com.dd.callmonitor.app.components.server.StartServerUseCaseImpl
import com.dd.callmonitor.app.components.server.StopServerUseCaseImpl
import com.dd.callmonitor.domain.calllog.ObserveCallLogUseCase
import com.dd.callmonitor.domain.callstatus.ObserveCallStatusUseCase
import com.dd.callmonitor.domain.connectivity.FormatHostAndPortUseCase
import com.dd.callmonitor.domain.connectivity.IsActiveNetworkWifiUseCase
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase
import com.dd.callmonitor.domain.server.StartServerUseCase
import com.dd.callmonitor.domain.server.StopServerUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinDomainModule() = module {

    factory { CheckPermissionUseCase(androidContext()) }

    factory { FormatHostAndPortUseCase() }

    factory { ObserveCallLogUseCase(callLogRepository = get()) }

    factory { ObserveCallStatusUseCase(callStatusRepository = get()) }

    factory { NormalizePhoneNumberUseCase(locale = get()) }

    single {
        val connectivityManager = androidContext().getSystemService(ConnectivityManager::class.java)
        ObserveWifiConnectivityUseCase(
            connectivityManager = connectivityManager,
            isActiveNetworkWifiUseCase = IsActiveNetworkWifiUseCase(connectivityManager)
        )
    }

    factory<StartServerUseCase> { StartServerUseCaseImpl(androidContext()) }

    factory<StopServerUseCase> { StopServerUseCaseImpl(androidContext()) }
}
