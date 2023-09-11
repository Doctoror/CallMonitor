package com.dd.callmonitor.data.calllog

import android.content.ContentResolver
import android.provider.CallLog.Calls
import com.dd.callmonitor.data.callstatus.ContactNameDataSource
import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.calllog.CallLogError
import com.dd.callmonitor.domain.calllog.CallLogRepository
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase
import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class CallLogRepositoryImpl(
    private val contactNameDataSource: ContactNameDataSource,
    private val contentResolver: ContentResolver,
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase,
    private val timesQueriedDataSource: TimesQueriedDataSource
) : CallLogRepository {

    override suspend fun getCallLog(): Either<CallLogError, List<CallLogEntry>> =
        checkPermissionUseCase(
            permission = ApiLevelPermissions.READ_CALL_LOG,
            whenDenied = { Either.left(CallLogError.PERMISSION_DENIED) },
            whenGranted = { getCallLogWithPermission() },
        )

    private suspend fun getCallLogWithPermission(): Either<CallLogError, List<CallLogEntry>> =
        withContext(Dispatchers.IO) {
            contentResolver
                .query(
                    Calls.CONTENT_URI,
                    arrayOf(
                        Calls._ID,
                        Calls.DATE,
                        Calls.DURATION,
                        Calls.NUMBER,
                        Calls.CACHED_NAME
                    ),
                    null,
                    null,
                    "${Calls.DATE} DESC"
                )
                .use { it ->
                    if (it == null) {
                        return@use Either.left(CallLogError.QUERY_FAILED)
                    }

                    val output = ArrayList<CallLogEntry>(it.count)
                    if (it.moveToFirst()) {
                        while (!it.isAfterLast) {
                            val id = it.getLong(it.getColumnIndexOrThrow(Calls._ID))

                            val number = normalizePhoneNumberUseCase(
                                it.getString(it.getColumnIndexOrThrow(Calls.NUMBER))
                            )

                            val cachedName = it.getString(
                                it.getColumnIndexOrThrow(Calls.CACHED_NAME)
                            ) ?: ""

                            val contactName = contactNameDataSource
                                .getContactNameByPhoneNumber(number)
                                .ifBlank { cachedName }

                            output.add(
                                CallLogEntry(
                                    beginningMillisUtc = it.getLong(
                                        it.getColumnIndexOrThrow(
                                            Calls.DATE
                                        )
                                    ),
                                    durationSeconds = it.getLong(
                                        it.getColumnIndexOrThrow(
                                            Calls.DURATION
                                        )
                                    ),
                                    number = number,
                                    name = contactName,
                                    // Note for reviewers: this also means a query from UI
                                    // (what is displayed by ContentCallLog) also updates
                                    // timesQueried. Assuming this is a requirement.
                                    timesQueried = timesQueriedDataSource.incrementAndGet(id)
                                )
                            )
                            it.moveToNext()
                        }
                    }

                    return@use Either.right(output)
                }
        }
}
