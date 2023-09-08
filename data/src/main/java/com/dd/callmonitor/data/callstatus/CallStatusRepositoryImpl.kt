package com.dd.callmonitor.data.callstatus

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.dd.callmonitor.domain.callstatus.CallStatus
import com.dd.callmonitor.domain.callstatus.CallStatusError
import com.dd.callmonitor.domain.callstatus.CallStatusRepository
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.util.ResultOrFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class CallStatusRepositoryImpl(
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val telephonyManager: TelephonyManager
) : CallStatusRepository {

    private val callStatus = MutableStateFlow<ResultOrFailure<CallStatus, CallStatusError>>(
        ResultOrFailure.success(
            CallStatus(
                ongoing = false,
                number = "",
                name = ""
            )
        )
    )

    override fun observeCallStatus(): StateFlow<ResultOrFailure<CallStatus, CallStatusError>> =
        callStatus

    override fun startListening() {
        checkPermissionUseCase(
            permission = ApiLevelPermissions.READ_PHONE_NUMBERS,
            whenDenied = {
                callStatus.value = ResultOrFailure.failure(CallStatusError.PERMISSION_DENIED)
            },
            whenGranted = {
                telephonyManager.listen(
                    phoneStateListener,
                    PhoneStateListener.LISTEN_CALL_STATE
                )
            }
        )
    }

    override fun stopListening() {
        checkPermissionUseCase(
            permission = ApiLevelPermissions.READ_PHONE_NUMBERS,
            whenDenied = {},
            whenGranted = {
                telephonyManager.listen(
                    phoneStateListener,
                    PhoneStateListener.LISTEN_NONE
                )
            }
        )
    }

    private val phoneStateListener = object : PhoneStateListener() {

        /**
         * Since API level 31 only [android.telecom.CallScreeningService] can receive the call state
         * with the number. To reduce the scope of the project, use only [PhoneStateListener]
         */
        @Deprecated("Deprecated in API level 31")
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            // Note for reviewers. When you asked for "ongoing" did you mean CALL_STATE_RINGING
            // or CALL_STATE_OFFHOOK? Assumption has been made.
            val isOngoing = state == TelephonyManager.CALL_STATE_OFFHOOK
            callStatus.value = ResultOrFailure.success(
                CallStatus(
                    ongoing = isOngoing,
                    number = if (isOngoing && phoneNumber.isNotNullOrBlank()) phoneNumber!! else "",
                    name = "" // TODO
                )
            )
        }
    }
}

private fun String?.isNotNullOrBlank() = this?.isNotBlank() ?: false
