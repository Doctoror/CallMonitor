package com.dd.callmonitor.data.callstatus

import android.content.ContentResolver
import android.telephony.TelephonyManager
import com.dd.callmonitor.domain.callstatus.CallStatusRepository
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase

/**
 * Note for reviewers: the goal is to encapsulate internal CallStatusRepositoryFactory in the data
 * module, and because data module does not have DI, a some kind of factory is necessary.
 */
class CallStatusRepositoryFactory {

    fun newInstance(
        checkPermissionUseCase: CheckPermissionUseCase,
        contentResolver: ContentResolver,
        normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase,
        telephonyManager: TelephonyManager,
    ): CallStatusRepository = CallStatusRepositoryImpl(
        checkPermissionUseCase,
        ContactNameDataSource(
            checkPermissionUseCase,
            contentResolver,
            normalizePhoneNumberUseCase
        ),
        normalizePhoneNumberUseCase,
        telephonyManager,
    )
}
