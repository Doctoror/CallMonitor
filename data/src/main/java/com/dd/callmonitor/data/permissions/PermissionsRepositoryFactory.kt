package com.dd.callmonitor.data.permissions

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.dd.callmonitor.domain.permissions.PermissionsRepository

private val Context.permissionsPreferencesDataStore by preferencesDataStore(
    "permissionsPreferencesDataStore"
)

/**
 * Note for code reviewers: the factory is required to keep PermissionsRepositoryImpl
 * internal in this Gradle module and at the same time avoiding adding DI to this module
 */
class PermissionsRepositoryFactory {

    fun newInstance(context: Context): PermissionsRepository =
        PermissionsRepositoryImpl(context.permissionsPreferencesDataStore)
}
