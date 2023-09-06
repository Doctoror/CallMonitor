package com.dd.callmonitor.app.di

import com.dd.callmonitor.data.permissions.PermissionsRepositoryFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun koinDataModule() = module {

    single { PermissionsRepositoryFactory().newInstance(androidContext()) }

}
