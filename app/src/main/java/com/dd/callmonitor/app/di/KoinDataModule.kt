package com.dd.callmonitor.app.di

import android.telephony.TelephonyManager
import com.dd.callmonitor.data.calllog.CallLogRepositoryFactory
import com.dd.callmonitor.data.callstatus.CallStatusRepositoryFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinDataModule() = module {

    factory {
        CallLogRepositoryFactory().newInstance(
            contentResolver = androidContext().contentResolver,
            checkPermissionUseCase = get()
        )
    }

    single {
        CallStatusRepositoryFactory().newInstance(
            checkPermissionUseCase = get(),
            telephonyManager = androidContext().getSystemService(TelephonyManager::class.java)
        )
    }
}
