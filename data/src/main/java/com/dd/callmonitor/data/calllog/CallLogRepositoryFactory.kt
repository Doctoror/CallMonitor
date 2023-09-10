package com.dd.callmonitor.data.calllog

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.dd.callmonitor.domain.calllog.CallLogRepository
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase

private val Context.timesQueriedDataStore by preferencesDataStore(
    "timesQueriedDataStore"
)

/**
 * Note for reviewers: the goal is to encapsulate internal CallLogRepositoryImpl in the data module,
 * and because data module does not have DI, a some kind of factory is necessary.
 */
class CallLogRepositoryFactory {

    fun newInstance(
        context: Context,
        checkPermissionUseCase: CheckPermissionUseCase,
    ): CallLogRepository = CallLogRepositoryImpl(
        context.contentResolver,
        checkPermissionUseCase,
        TimesQueriedDataSource(context.timesQueriedDataStore)
    )
}
