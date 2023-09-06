package com.dd.callmonitor.data.calls

import android.content.ContentResolver
import android.provider.CallLog.Calls
import com.dd.callmonitor.domain.calls.CallLogEntry
import com.dd.callmonitor.domain.calls.CallStatus
import com.dd.callmonitor.domain.calls.CallsRepository

internal class CallsRepositoryImpl(
    // private val telephonyManager: TelephonyManager TODO
    private val contentResolver: ContentResolver
) : CallsRepository {

    override fun getCallLog(): List<CallLogEntry> {
        contentResolver
            .query(
                Calls.CONTENT_URI,
                arrayOf(
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
                    return emptyList()
                }

                val output = ArrayList<CallLogEntry>(it.count)
                if (it.moveToFirst()) {
                    while (!it.isAfterLast) {
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
                                timesQueried = 0 // TODO populate
                            )
                        )
                        it.moveToNext()
                    }
                }

                return output
            }
    }

    // TODO implement
    override fun getStatus(): CallStatus = CallStatus(
        false,
        "Fake number",
        "Fake name"
    )
}
