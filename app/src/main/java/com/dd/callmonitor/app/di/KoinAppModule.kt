package com.dd.callmonitor.app.di

import android.app.NotificationManager
import android.content.ContentResolver
import android.content.res.Resources
import com.dd.callmonitor.app.notifications.NotificationChannelRegistrator
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.Locale

fun koinAppModule() = module {

    factory<ContentResolver> { androidContext().contentResolver }

    single { Dispatchers }

    factory { Locale.getDefault() }

    factory {
        NotificationChannelRegistrator(
            notificationManager = androidContext().getSystemService(NotificationManager::class.java),
            resources = get()
        )
    }

    factory<Resources> { androidContext().resources }
}
