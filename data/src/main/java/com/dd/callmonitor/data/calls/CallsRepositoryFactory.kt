package com.dd.callmonitor.data.calls

import android.content.ContentResolver
import com.dd.callmonitor.domain.calls.CallsRepository

class CallsRepositoryFactory {

    fun newInstance(contentResolver: ContentResolver): CallsRepository =
        CallsRepositoryImpl(contentResolver)
}
