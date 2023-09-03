package com.dd.callmonitor.data.notifications

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class NotificationPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private val preferenceKeyPermissionRationaleShown =
        booleanPreferencesKey("preferenceKeyPermissionRationaleShown")

    suspend fun wasPermissionRationaleShown(): Boolean = dataStore
        .data
        .map { it[preferenceKeyPermissionRationaleShown] ?: false }
        .first()

    suspend fun setPermissionRationaleShown(shown: Boolean) {
        dataStore.edit {
            it[preferenceKeyPermissionRationaleShown] = shown
        }
    }
}
