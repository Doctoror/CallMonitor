package com.dd.callmonitor.data.calllog

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class TimesQueriedDataSource(private val dataStore: DataStore<Preferences>) {

    suspend fun incrementAndGet(id: Long): Int {
        val key = intPreferencesKey(id.toString())

        dataStore.updateData {
            it.toMutablePreferences().apply {
                set(key, (get(key) ?: 0) + 1)
            }
        }

        return dataStore.data.map { it[key]!! }.first()
    }
}
