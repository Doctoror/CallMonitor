package com.dd.callmonitor.framework

import android.net.ConnectivityManager
import com.dd.callmonitor.domain.ObserveWifiConnectivityUseCase
import com.dd.callmonitor.framework.lifecycle.ActivityMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun koinFrameworkModule() = module {

    singleOf(::ActivityMonitor)

    single {
        ObserveWifiConnectivityUseCase(
            androidContext().getSystemService(ConnectivityManager::class.java)
        )
    }

    factory { androidContext().resources }
}
