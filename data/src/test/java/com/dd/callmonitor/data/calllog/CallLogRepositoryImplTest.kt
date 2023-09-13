package com.dd.callmonitor.data.calllog

import android.Manifest
import android.content.ContentProvider
import android.content.ContentValues
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.CallLog
import com.dd.callmonitor.data.callstatus.ContactNameDataSource
import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.calllog.CallLogError
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase
import com.dd.callmonitor.domain.util.Either
import com.dd.callmonitor.domain.util.Optional
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class CallLogRepositoryImplTest {

    private val context = RuntimeEnvironment.getApplication()
    private val contextShadow = Shadows.shadowOf(context)
    private val contactNameDataSource: ContactNameDataSource = mockk()
    private val contentResolver = context.contentResolver
    private val checkPermissionUseCase = CheckPermissionUseCase(context)
    private val normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase = mockk()
    private val timesQueriedDataSource: TimesQueriedDataSource = mockk()

    private val underTest = CallLogRepositoryImpl(
        contactNameDataSource,
        contentResolver,
        checkPermissionUseCase,
        Dispatchers.Unconfined,
        normalizePhoneNumberUseCase,
        timesQueriedDataSource
    )

    @Test
    fun getCallLogReturnsErrorPermissionDeniedWhenDenied() = runTest {
        assertEquals(
            Either.left<CallLogError, List<CallLogEntry>>(CallLogError.PERMISSION_DENIED),
            underTest.getCallLog()
        )
    }

    @Test
    fun getCallLogReturnsErrorQueryFailedWhenQueryReturnedNull() = runTest {
        contextShadow.grantPermissions(Manifest.permission.READ_CALL_LOG)

        assertEquals(
            Either.left<CallLogError, List<CallLogEntry>>(CallLogError.QUERY_FAILED),
            underTest.getCallLog()
        )
    }

    @Test
    fun getCallLogReturnsEmptyListWhenQueryReturnedEmptyCursor() = runTest {
        registerContentProvider(emptyArray())

        contextShadow.grantPermissions(Manifest.permission.READ_CALL_LOG)

        assertEquals(
            Either.right<CallLogError, List<CallLogEntry>>(emptyList()),
            underTest.getCallLog()
        )
    }

    @Test
    fun getCallLogReturnsCallLogFromContentResolver() = runTest {
        val id1 = 1L
        val date1 = 2L
        val duration1 = 3L
        val number1 = "Number 1"
        val cachedName1 = Optional.of("Cached Name 1")

        val id2 = 4L
        val date2 = 5L
        val duration2 = 6L
        val number2 = "Number 2"
        val cachedName2 = Optional.of("Cached Name 2")

        val timesQueried1 = 7
        val timesQueried2 = 8

        val number1Normalized = "Number 1 normalized"
        val number2Normalized = "Number 2 normalized"

        val contactName2 = Optional.of("Contact Name 2")

        every { normalizePhoneNumberUseCase(number1) } returns number1Normalized
        every { normalizePhoneNumberUseCase(number2) } returns number2Normalized

        coEvery {
            contactNameDataSource.getContactNameByPhoneNumber(Optional.of(number1Normalized))
        } returns Optional.empty()

        coEvery {
            contactNameDataSource.getContactNameByPhoneNumber(Optional.of(number2Normalized))
        } returns contactName2

        coEvery { timesQueriedDataSource.incrementAndGet(id1) } returns timesQueried1
        coEvery { timesQueriedDataSource.incrementAndGet(id2) } returns timesQueried2

        registerContentProvider(
            arrayOf(
                arrayOf(id1, date1, duration1, number1, cachedName1.get()),
                arrayOf(id2, date2, duration2, number2, cachedName2.get())
            )
        )

        contextShadow.grantPermissions(Manifest.permission.READ_CALL_LOG)

        assertEquals(
            Either.right<CallLogError, List<CallLogEntry>>(
                listOf(
                    CallLogEntry(
                        beginningMillisUtc = date1,
                        durationSeconds = duration1,
                        number = Optional.of(number1Normalized),
                        name = cachedName1,
                        timesQueried = timesQueried1
                    ),

                    CallLogEntry(
                        beginningMillisUtc = date2,
                        durationSeconds = duration2,
                        number = Optional.of(number2Normalized),
                        name = contactName2,
                        timesQueried = timesQueried2
                    )
                )
            ),
            underTest.getCallLog()
        )
    }

    private fun registerContentProvider(rows: Array<Array<Any>>) {
        val info = ProviderInfo().apply { authority = CallLog.AUTHORITY }
        info.authority = CallLog.AUTHORITY

        CallsContentProvider.rows = rows

        Robolectric
            .buildContentProvider(CallsContentProvider::class.java)
            .create(ProviderInfo().apply { authority = CallLog.AUTHORITY })
    }
}

private class CallsContentProvider : SimpleContentProvider() {

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor = MatrixCursor(projection ?: emptyArray()).apply {
        rows.forEach(::addRow)
    }

    companion object {

        var rows = arrayOf<Array<Any>>()
    }
}

private open class SimpleContentProvider : ContentProvider() {

    override fun onCreate() = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        throw UnsupportedOperationException()
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException()
    }
}
