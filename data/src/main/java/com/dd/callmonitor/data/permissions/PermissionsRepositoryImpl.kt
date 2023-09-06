package com.dd.callmonitor.data.permissions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.dd.callmonitor.domain.permissions.PermissionsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class PermissionsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : PermissionsRepository {

    override suspend fun setPermissionRationaleShown(permission: String, shown: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(permission)] = shown
        }
    }

    override suspend fun wasPermissionRationaleShown(permission: String): Boolean = dataStore
        .data
        .map { it[booleanPreferencesKey(permission)] ?: false }
        .first()
}
