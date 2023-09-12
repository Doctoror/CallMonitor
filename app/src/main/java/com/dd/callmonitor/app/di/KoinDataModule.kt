package com.dd.callmonitor.app.di

import android.telephony.TelephonyManager
import com.dd.callmonitor.data.calllog.CallLogRepositoryFactory
import com.dd.callmonitor.data.callstatus.CallStatusRepositoryFactory
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinDataModule() = module {

    factory {
        val context = androidContext()
        CallLogRepositoryFactory().newInstance(
            context = context,
            contentResolver = get(),
            checkPermissionUseCase = CheckPermissionUseCase(context),
            dispatcherIo = get<Dispatchers>().IO,
            normalizePhoneNumberUseCase = get()
        )
    }

    single {
        CallStatusRepositoryFactory().newInstance(
            checkPermissionUseCase = get(),
            contentResolver = get(),
            dispatcherIo = get<Dispatchers>().IO,
            normalizePhoneNumberUseCase = get(),
            telephonyManager = androidContext().getSystemService(TelephonyManager::class.java)
        )
    }
}
