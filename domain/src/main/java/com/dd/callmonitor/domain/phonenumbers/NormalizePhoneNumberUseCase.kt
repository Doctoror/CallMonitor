package com.dd.callmonitor.domain.phonenumbers

import android.telephony.PhoneNumberUtils
import java.util.Locale

class NormalizePhoneNumberUseCase(private val locale: Locale) {

    operator fun invoke(number: String?): String {
        val normalized = PhoneNumberUtils.normalizeNumber(number)
        return if (normalized.isBlank()) {
            normalized
        } else {
            PhoneNumberUtils
                .formatNumberToE164(normalized, locale.country)
                ?: normalized
        }
    }
}
