package com.dd.callmonitor.data.calllog

import android.content.ContentResolver
import android.provider.CallLog.Calls
import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.calllog.CallLogError
import com.dd.callmonitor.domain.calllog.CallLogRepository
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.util.ResultOrFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class CallLogRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val timesQueriedDataSource: TimesQueriedDataSource
) : CallLogRepository {

    override suspend fun getCallLog(): ResultOrFailure<List<CallLogEntry>, CallLogError> =
        checkPermissionUseCase(
            permission = ApiLevelPermissions.READ_CALL_LOG,
            whenDenied = {
                ResultOrFailure.failure(
                    CallLogError.PERMISSION_DENIED
                )
            },
            whenGranted = { getCallLogWithPermission() },
        )

    private suspend fun getCallLogWithPermission(): ResultOrFailure<List<CallLogEntry>, CallLogError> =
        withContext(Dispatchers.IO) {
            contentResolver
                .query(
                    Calls.CONTENT_URI,
                    arrayOf(
                        Calls._ID,
                        Calls.DATE,
                        Calls.DURATION,
                        Calls.CACHED_NAME,
                        Calls.CACHED_NORMALIZED_NUMBER
                    ),
                    null,
                    null,
                    "${Calls.DATE} DESC"
                )
                .use {
                    if (it == null) {
                        return@use ResultOrFailure.failure(CallLogError.QUERY_FAILED)
                    }

                    val output = ArrayList<CallLogEntry>(it.count)
                    if (it.moveToFirst()) {
                        while (!it.isAfterLast) {
                            val id = it.getLong(it.getColumnIndexOrThrow(Calls._ID))
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
                                    number = it.getString(
                                        it.getColumnIndexOrThrow(
                                            Calls.CACHED_NORMALIZED_NUMBER
                                        )
                                    ) ?: "",
                                    name = it.getString(
                                        it.getColumnIndexOrThrow(
                                            Calls.CACHED_NAME
                                        )
                                    ) ?: "",
                                    // Note for reviewers: this also means a query from UI
                                    // (what is displayed by ContentCallLog) also updates
                                    // timesQueried
                                    timesQueried = timesQueriedDataSource.incrementAndGet(id)
                                )
                            )
                            it.moveToNext()
                        }
                    }

                    return@use ResultOrFailure.success(output)
                }
        }
}
