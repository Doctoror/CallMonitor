package com.dd.callmonitor.data.calllog

import android.content.ContentResolver
import com.dd.callmonitor.domain.calllog.CallLogRepository
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase

/**
 * Note for reviewers: the goal is to encapsulate internal CallLogRepositoryImpl in the data module,
 * and because data module does not have DI, a some kind of factory is necessary.
 */
class CallLogRepositoryFactory {

    fun newInstance(
        contentResolver: ContentResolver,
        checkPermissionUseCase: CheckPermissionUseCase,
    ): CallLogRepository = CallLogRepositoryImpl(contentResolver, checkPermissionUseCase)
}
