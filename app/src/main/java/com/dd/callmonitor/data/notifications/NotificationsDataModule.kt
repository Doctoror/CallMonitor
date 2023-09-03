package com.dd.callmonitor.data.notifications

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.notificationPreferencesDataStore by preferencesDataStore(
    "notificationPreferencesDataStore"
)

fun koinNotificationsDataModule() = module {

    single {
        NotificationPreferencesRepository(
            androidContext().notificationPreferencesDataStore
        )
    }
}
