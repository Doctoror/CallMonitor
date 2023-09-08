package com.dd.callmonitor.app.di

import android.net.ConnectivityManager
import com.dd.callmonitor.app.components.server.StartServerUseCaseImpl
import com.dd.callmonitor.app.components.server.StopServerUseCaseImpl
import com.dd.callmonitor.domain.calllog.GetCallLogUseCase
import com.dd.callmonitor.domain.callstatus.CallStatusStartListeningUseCase
import com.dd.callmonitor.domain.callstatus.CallStatusStopListeningUseCase
import com.dd.callmonitor.domain.callstatus.GetCallStatusUseCase
import com.dd.callmonitor.domain.connectivity.FormatHostAndPortUseCase
import com.dd.callmonitor.domain.connectivity.IsActiveNetworkWifiUseCase
import com.dd.callmonitor.domain.connectivity.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.server.StartServerUseCase
import com.dd.callmonitor.domain.server.StopServerUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.Locale

fun koinDomainModule() = module {

    factory { CallStatusStartListeningUseCase(callStatusRepository = get()) }

    factory { CallStatusStopListeningUseCase(callStatusRepository = get()) }

    factory { CheckPermissionUseCase(androidContext()) }

    factory { FormatHostAndPortUseCase() }

    factory { GetCallLogUseCase(callLogRepository = get()) }

    factory { GetCallStatusUseCase(callStatusRepository = get()) }

    factory { Locale.getDefault() }

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
