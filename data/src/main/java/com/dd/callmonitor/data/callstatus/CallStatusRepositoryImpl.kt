package com.dd.callmonitor.data.callstatus

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.dd.callmonitor.domain.callstatus.CallStatus
import com.dd.callmonitor.domain.callstatus.CallStatusError
import com.dd.callmonitor.domain.callstatus.CallStatusRepository
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase
import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class CallStatusRepositoryImpl(
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val contactNameDataSource: ContactNameDataSource,
    private val normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase,
    private val telephonyManager: TelephonyManager
) : CallStatusRepository {

    private val callStatus = MutableStateFlow<Either<CallStatusError, CallStatus>>(
        Either.right(
            CallStatus(
                ongoing = false,
                number = "",
                name = ""
            )
        )
    )

    override fun observeCallStatus(): StateFlow<Either<CallStatusError, CallStatus>> =
        callStatus

    override fun startListening() {
        checkPermissionUseCase(
            permission = ApiLevelPermissions.READ_PHONE_NUMBERS,
            whenDenied = {
                callStatus.value = Either.left(CallStatusError.PERMISSION_DENIED)
            },
            whenGranted = {
                phoneStateListener.createContext()

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

                phoneStateListener.cancelContext()
            }
        )
    }

    private val phoneStateListener = object : PhoneStateListener() {

        private val lock = Any()

        private var context: CoroutineContext? = null

        private var scope: CoroutineScope? = null

        fun createContext() {
            cancelContext()
            synchronized(lock) {
                context = SupervisorJob() + Dispatchers.Main.immediate
                scope = CoroutineScope(context!!)
            }
        }

        fun cancelContext() {
            synchronized(lock) {
                if (context != null) {
                    context!!.cancel()
                }

                context = null
                scope = null
            }
        }

        /**
         * Since API level 31 only [android.telecom.CallScreeningService] can receive the call state
         * with the number. To reduce the scope of the project, use only [PhoneStateListener]
         */
        @Deprecated("Deprecated in API level 31")
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            // Note for reviewers. When you asked for "ongoing" did you mean CALL_STATE_RINGING
            // or CALL_STATE_OFFHOOK? Assumption has been made.
            if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                emitCallStatusForOngoingCall(normalizePhoneNumberUseCase(phoneNumber))
            } else {
                emitEmptyStatus()
            }
        }

        private fun emitCallStatusForOngoingCall(phoneNumber: String) {
            synchronized(lock) {
                requireNotNull(scope) { "Must be called after createContext" }
                    .launch {
                        callStatus.value = Either.right(
                            CallStatus(
                                ongoing = true,
                                number = phoneNumber,
                                name = contactNameDataSource
                                    .getContactNameByPhoneNumber(phoneNumber)
                                    .orElse("")
                            )
                        )
                    }
            }
        }

        private fun emitEmptyStatus() {
            callStatus.value = Either.right(
                CallStatus(
                    ongoing = false,
                    number = "",
                    name = ""
                )
            )
        }
    }
}
