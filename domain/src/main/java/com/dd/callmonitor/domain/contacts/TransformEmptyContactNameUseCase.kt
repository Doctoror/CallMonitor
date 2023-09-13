package com.dd.callmonitor.domain.contacts

import android.content.res.Resources
import com.dd.callmonitor.domain.R
import com.dd.callmonitor.domain.util.Optional

class TransformEmptyContactNameUseCase(private val resources: Resources) {

    /**
     *
     * @return
     * - [R.string.call_log_stub_unknown] if `contactName` is empty
     * - value supplied as `contactName` argument if it's not empty
     */
    operator fun invoke(contactName: Optional<String>): String = contactName.orElse(
        resources.getString(R.string.call_log_stub_unknown)
    )!!
}
