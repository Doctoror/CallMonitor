package com.dd.callmonitor.data.callstatus

import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.annotation.WorkerThread
import com.dd.callmonitor.domain.callstatus.CallStatus
import com.dd.callmonitor.domain.callstatus.CallStatusError
import com.dd.callmonitor.domain.callstatus.CallStatusRepository
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase
import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

internal class CallStatusRepositoryImpl(
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val contactNameDataSource: ContactNameDataSource,
    private val normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase,
    private val telephonyManager: TelephonyManager
) : CallStatusRepository {

    override fun observeCallStatus(): Flow<Either<CallStatusError, CallStatus>> = callbackFlow {
        // Because of the horrible design of pre api-level 29 PhoneStateListener constructor which
        // can only be instantiated by a Looper thread, we have no choice except creating a
        // PhoneStateListener in a new thread with a Looper.
        val phoneStateListenerLatch = CountDownLatch(1)
        val phoneStateListener = AtomicReference<PhoneStateListener>()
        val thread = object : Thread() {

            @Volatile
            private var looper: Looper? = null

            override fun run() {
                Looper.prepare()
                looper = Looper.myLooper()
                phoneStateListener.set(
                    ChannelSendingPhoneStateListener(
                        contactNameDataSource,
                        normalizePhoneNumberUseCase,
                        this@callbackFlow
                    )
                )
                phoneStateListenerLatch.countDown()
                Looper.loop()
            }

            override fun interrupt() {
                looper?.quitSafely()
                super.interrupt()
            }
        }

        try {
            thread.start()

            // Await PhoneStateListener initialization by the Looper thread
            phoneStateListenerLatch.await()

            checkPermissionUseCase(
                permission = ApiLevelPermissions.READ_PHONE_NUMBERS,
                whenDenied = {
                    trySend(Either.left(CallStatusError.PERMISSION_DENIED))
                },
                whenGranted = {
                    telephonyManager.listen(
                        phoneStateListener.get()!!,
                        PhoneStateListener.LISTEN_CALL_STATE
                    )
                }
            )
        } catch (e: InterruptedException) {
            thread.interrupt()
            throw e
        }

        awaitClose {
            telephonyManager.listen(
                phoneStateListener.get()!!,
                PhoneStateListener.LISTEN_NONE
            )

            thread.interrupt()
        }
    }

    private class ChannelSendingPhoneStateListener(
        private val contactNameDataSource: ContactNameDataSource,
        private val normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase,
        private val sendChannel: SendChannel<Either<CallStatusError, CallStatus>>
    ) : PhoneStateListener() {

        /**
         * Since API level 31 only [android.telecom.CallScreeningService] can receive the call
         * state with the number. To reduce the scope of the project, only [PhoneStateListener]
         * is used.
         */
        @Deprecated("Deprecated in API level 31")
        @WorkerThread
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            // Note for reviewers. When you asked for "ongoing" did you mean CALL_STATE_RINGING
            // or CALL_STATE_OFFHOOK? Assumption has been made.
            sendChannel.trySend(
                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    makeCallStatusForOngoingCall(normalizePhoneNumberUseCase(phoneNumber))
                } else {
                    makeEmptyCallStatus()
                }
            )
        }

        @WorkerThread
        private fun makeCallStatusForOngoingCall(
            phoneNumber: String
        ): Either<CallStatusError, CallStatus> = Either.right(
            CallStatus(
                ongoing = true,
                number = phoneNumber,
                // We are in a worker thread, runBlocking is intended
                name = runBlocking {
                    contactNameDataSource
                        .getContactNameByPhoneNumber(phoneNumber)
                        .orElse("")
                }
            )
        )

        private fun makeEmptyCallStatus(): Either<CallStatusError, CallStatus> = Either
            .right(
                CallStatus(
                    ongoing = false,
                    number = "",
                    name = ""
                )
            )
    }
}
