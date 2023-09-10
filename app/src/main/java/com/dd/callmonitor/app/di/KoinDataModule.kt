package com.dd.callmonitor.app.di

import android.telephony.TelephonyManager
import com.dd.callmonitor.data.calllog.CallLogRepositoryFactory
import com.dd.callmonitor.data.callstatus.CallStatusRepositoryFactory
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinDataModule() = module {

    factory {
        val context = androidContext()
        CallLogRepositoryFactory().newInstance(
            context = context,
            checkPermissionUseCase = CheckPermissionUseCase(context),
            normalizePhoneNumberUseCase = get()
        )
    }

    single {
        CallStatusRepositoryFactory().newInstance(
            checkPermissionUseCase = get(),
            contentResolver = get(),
            normalizePhoneNumberUseCase = get(),
            telephonyManager = androidContext().getSystemService(TelephonyManager::class.java),
            resources = get()
        )
    }
}
