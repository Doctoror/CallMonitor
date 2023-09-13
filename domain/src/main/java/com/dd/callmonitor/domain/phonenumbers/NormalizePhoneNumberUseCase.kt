package com.dd.callmonitor.domain.phonenumbers

import android.telephony.PhoneNumberUtils
import java.util.Locale

class NormalizePhoneNumberUseCase(private val locale: Locale) {

    /**
     * Normalize phone number to E164 format.
     *
     * If not possible, will only normalize by removing the spaces and extra characters like
     * whitespaces and dashes.
     */
    operator fun invoke(number: String): String {
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
