package com.dd.callmonitor.domain.contacts

import android.content.res.Resources
import com.dd.callmonitor.domain.R

class TransformEmptyContactNameUseCase(private val resources: Resources) {

    /**
     * @return
     * - [R.string.call_log_stub_unknown] if `contactName` argument is empty
     * - value supplied as `contactName` argument if it's not empty
     */
    operator fun invoke(contactName: String): String = contactName.ifBlank {
        resources.getString(R.string.call_log_stub_unknown)
    }
}
