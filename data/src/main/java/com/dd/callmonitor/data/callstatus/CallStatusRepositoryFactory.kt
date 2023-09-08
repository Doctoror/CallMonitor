package com.dd.callmonitor.data.callstatus

import android.telephony.TelephonyManager
import com.dd.callmonitor.domain.callstatus.CallStatusRepository
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase

/**
 * Note for reviewers: the goal is to encapsulate internal CallStatusRepositoryFactory in the data
 * module, and because data module does not have DI, a some kind of factory is necessary.
 */
class CallStatusRepositoryFactory {

    fun newInstance(
        checkPermissionUseCase: CheckPermissionUseCase,
        telephonyManager: TelephonyManager
    ): CallStatusRepository = CallStatusRepositoryImpl(
        checkPermissionUseCase,
        telephonyManager
    )
}
