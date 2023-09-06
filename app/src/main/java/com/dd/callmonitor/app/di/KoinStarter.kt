package com.dd.callmonitor.app.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun startKoin(context: Context) = startKoin {
    androidContext(context)
    modules(
        koinDataModule(),
        koinDomainModule(),
        koinAppModule(),
        koinMainModule(),
        koinServerModule()
    )
}
