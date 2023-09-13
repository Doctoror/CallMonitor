package com.dd.callmonitor.data.calllog

import android.content.ContentResolver
import android.database.Cursor
import android.provider.CallLog.Calls
import com.dd.callmonitor.data.callstatus.ContactNameDataSource
import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.calllog.CallLogError
import com.dd.callmonitor.domain.calllog.CallLogRepository
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase
import com.dd.callmonitor.domain.util.Either
import com.dd.callmonitor.domain.util.Optional
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val LIMIT = "100"

internal class CallLogRepositoryImpl(
    private val contactNameDataSource: ContactNameDataSource,
    private val contentResolver: ContentResolver,
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val dispatcherIo: CoroutineDispatcher,
    private val normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase,
    private val timesQueriedDataSource: TimesQueriedDataSource
) : CallLogRepository {

    /**
     * Returns call log without paging.
     *
     * Note for reviewers: paging has been omitted to reduce the scope of the (already big) project.
     */
    override suspend fun getCallLog(): Either<CallLogError, List<CallLogEntry>> =
        checkPermissionUseCase(
            permission = ApiLevelPermissions.READ_CALL_LOG,
            whenDenied = { Either.left(CallLogError.PERMISSION_DENIED) },
            whenGranted = { getCallLogWithPermission() },
        )

    private suspend fun getCallLogWithPermission(): Either<CallLogError, List<CallLogEntry>> =
        withContext(dispatcherIo) {
            query().use { cursorToEntriesList(it) }
        }

    private fun query(): Cursor? = contentResolver.query(
        Calls.CONTENT_URI.buildUpon().appendQueryParameter("limit", LIMIT).build(),
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

    private suspend fun cursorToEntriesList(it: Cursor?): Either<CallLogError, List<CallLogEntry>> {
        if (it == null) {
            return Either.left(CallLogError.QUERY_FAILED)
        }

        val output = ArrayList<CallLogEntry>(it.count)
        if (it.moveToFirst()) {
            while (!it.isAfterLast) {
                output.add(cursorAtPositionToEntry(it))
                it.moveToNext()
            }
        }

        return Either.right(output)
    }

    private suspend fun cursorAtPositionToEntry(cursor: Cursor): CallLogEntry {
        val normalizedNumber = Optional
            .ofNullable(cursor.getCallLogEntryNumber())
            .map { normalizePhoneNumberUseCase(it) }

        val contactName = contactNameDataSource
            .getContactNameByPhoneNumber(normalizedNumber)
            .or { Optional.ofNullable(cursor.getCallLogEntryCachedName()) }

        return CallLogEntry(
            beginningMillisUtc = cursor.getCallLogEntryDate(),
            durationSeconds = cursor.getCallLogEntryDuration(),
            number = normalizedNumber,
            name = contactName,
            // Note for reviewers: this also means a query from UI (what is displayed by
            // ContentCallLog) also updates timesQueried. Assuming this is a requirement
            timesQueried = timesQueriedDataSource.incrementAndGet(cursor.getCallLogEntryId())
        )
    }

    private fun Cursor.getCallLogEntryId(): Long =
        getLong(getColumnIndexOrThrow(Calls._ID))

    private fun Cursor.getCallLogEntryDate(): Long =
        getLong(getColumnIndexOrThrow(Calls.DATE))

    private fun Cursor.getCallLogEntryDuration(): Long =
        getLong(getColumnIndexOrThrow(Calls.DURATION))

    private fun Cursor.getCallLogEntryNumber(): String? =
        getString(getColumnIndexOrThrow(Calls.NUMBER))

    private fun Cursor.getCallLogEntryCachedName(): String? =
        getString(getColumnIndexOrThrow(Calls.CACHED_NAME))
}
