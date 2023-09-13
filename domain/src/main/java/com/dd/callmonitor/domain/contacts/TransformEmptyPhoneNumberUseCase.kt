package com.dd.callmonitor.domain.contacts

import android.content.res.Resources
import com.dd.callmonitor.domain.R
import com.dd.callmonitor.domain.util.Optional

class TransformEmptyPhoneNumberUseCase(private val resources: Resources) {

    /**
     * @return
     * - [R.string.call_number_stub_unspecified] if `phoneNumber` is empty
     * - value supplied as `phoneNumber` argument if it's not empty
     */
    operator fun invoke(phoneNumber: Optional<String>): String = phoneNumber.orElse(
        resources.getString(R.string.call_number_stub_unspecified)
    )
}
