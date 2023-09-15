package com.dd.callmonitor.data.calllog

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.provider.CallLog.Calls
import androidx.annotation.WorkerThread
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

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
     * Emits call log without paging.
     *
     * Note for reviewers: paging has been omitted to reduce the scope of the (already big) project.
     */
    override fun observeCallLog(): Flow<Either<CallLogError, List<CallLogEntry>>> =
        checkPermissionUseCase(
            permission = ApiLevelPermissions.READ_CALL_LOG,
            whenDenied = { flowOf(Either.left(CallLogError.PERMISSION_DENIED)) },
            whenGranted = { getCallLogWithPermission() },
        )

    private fun getCallLogWithPermission(): Flow<Either<CallLogError, List<CallLogEntry>>> =
        callbackFlow {

            fun runQueryAndTrySend() {
                trySend(query().use { cursorToEntriesList(it) })
            }

            runQueryAndTrySend()

            val contentObserver = object : ContentObserver(null) {

                override fun onChange(selfChange: Boolean) {
                    runQueryAndTrySend()
                }
            }

            contentResolver.registerContentObserver(
                Calls.CONTENT_URI,
                true,
                contentObserver
            )

            awaitClose {
                contentResolver.unregisterContentObserver(contentObserver)
            }
        }.flowOn(dispatcherIo)

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

    private fun cursorToEntriesList(it: Cursor?): Either<CallLogError, List<CallLogEntry>> {
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

    @WorkerThread
    private fun cursorAtPositionToEntry(cursor: Cursor): CallLogEntry {
        val normalizedNumber = Optional
            .ofNullable(cursor.getCallLogEntryNumber())
            .map { normalizePhoneNumberUseCase(it) }

        val contactName = runBlocking {
            contactNameDataSource
                .getContactNameByPhoneNumber(normalizedNumber)
                // Some implementations will return "" if no cached name, so we must convert this to
                // null as our logic relies on missing values to be Optional.empty()
                .or { Optional.ofNullable(cursor.getCallLogEntryCachedName()?.ifEmpty { null }) }
        }

        return CallLogEntry(
            beginningMillisUtc = cursor.getCallLogEntryDate(),
            durationSeconds = cursor.getCallLogEntryDuration(),
            number = normalizedNumber,
            name = contactName,
            // Note for reviewers: this also means a query from UI (what is displayed by
            // ContentCallLog) also updates timesQueried. Assuming this is a requirement
            timesQueried = runBlocking {
                timesQueriedDataSource.incrementAndGet(cursor.getCallLogEntryId())
            }
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
